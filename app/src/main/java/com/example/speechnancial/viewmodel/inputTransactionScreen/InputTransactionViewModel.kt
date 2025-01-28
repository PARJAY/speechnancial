package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.tools.createTransactionFromInput
import com.example.speechnancial.tools.startSpeechToText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class InputTransactionViewModel(
    private val transactionDao: TransactionDao,
    context: Context,
    private val dataStoreManager: WalletDataStoreManager
) : ViewModel() {

    private val _state = MutableStateFlow(InputTransactionState())
    val state = _state.asStateFlow()

    private val _speechRecognizer = MutableStateFlow(SpeechRecognizer.createSpeechRecognizer(context))

    private val _speechRecognizerIntent = MutableStateFlow(
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
        }
    )

    private suspend fun updateTotalIncome(amount: Float) {
        manipulateDataStore(
            action = { dataStoreManager.newIncomeInputed(amount) },
            errorMessage = "Error updating datastore income"
        )
    }

    private suspend fun updateTotalExpense(amount: Float) {
        manipulateDataStore(
            action = { dataStoreManager.newExpenseInputed(amount) },
            errorMessage = "Error updating datastore expense"
        )
    }

    private suspend fun setTotalOutcome(amount: Float) {
        manipulateDataStore(
            action = { dataStoreManager.setTotalOutcomeAndUpdateBalance(amount) },
            errorMessage = "Error updating setting Total Outcome"
        )
    }

    private suspend fun setTotalIncome(amount: Float) {
        manipulateDataStore(
            action = { dataStoreManager.setTotalIncomeAndUpdateBalance(amount) },
            errorMessage = "Error updating setting Total Income"
        )
    }

    fun onEvent(event : InputTransactionEvent) {
        when(event) {
            is InputTransactionEvent.IsEditExistingTransactionData -> {
                _state.update { it.copy(
                    proposedTransaction = event.transaction,
                    source = event.transaction.details?.joinToString(separator = "\n") { detail ->
                        "${detail.description} ${detail.nominal.toInt()} rupiah"
                    }?.ifEmpty { event.transaction.rawText }.toString(),
                    isEditExistingTransaction = true
                ) }
            }

            is InputTransactionEvent.IsCloseScreen -> {
                _state.update {
                    InputTransactionState()
                }
                event.navController.navigateUp()
            }

            is InputTransactionEvent.SpeechToTransactionButtonClicked -> {
                _state.update { currentState ->
                    if (!currentState.isTranscribing) {
                        currentState.copy(
                            isTranscribing = true,
                            isFinishedTranscribing = false
                        )
                    } else {
                        currentState.copy(
                            isTranscribing = false,
                            isFinishedTranscribing = true
                        )
                    }
                }

                if (_state.value.isTranscribing) {
                    startSpeechToText(
                        context = event.context,
                        speechRecognizer = _speechRecognizer.value,
                        speechRecognizerIntent = _speechRecognizerIntent.value,
                        onPartialResults = { partialResult ->
                            _state.update { it.copy(
                                source =
                                if (it.previousPartialResult.isNotEmpty() && partialResult.isEmpty())
                                    it.source + it.previousPartialResult
                                else it.source,
                                previousPartialResult = partialResult,
                            ) }
                        }
                    )

                    _speechRecognizer.value.startListening(_speechRecognizerIntent.value)

                } else {
                    _speechRecognizer.value.stopListening()
                    _speechRecognizer.value.destroy()

                    _state.update { it.copy(
                        source = it.source + it.previousPartialResult,
                        previousPartialResult = ""
                    ) }

                    _state.update { it.copy(
                        proposedTransaction = createTransactionFromInput(
                            it.source.lowercase(),
                            state.value.proposedTransaction.id
                        )
                    )}
                }
            }

            is InputTransactionEvent.HandleUserInput -> {
                _state.update { it.copy(
                    source = event.userInput,
                    previousPartialResult = ""
                ) }

                _state.update { it.copy(
                    proposedTransaction = createTransactionFromInput(
                        it.source.lowercase(),
                        state.value.proposedTransaction.id
                    )
                )}
            }

            InputTransactionEvent.ResetInput ->
                _state.update { InputTransactionState() }

            InputTransactionEvent.ReviseLaterCheckboxClicked -> {
                _state.update { it.copy(
                    isReviseNeeded = !it.isReviseNeeded
                ) }
            }

            InputTransactionEvent.TranscriptionErrorCheckboxClicked -> {
                _state.update { it.copy(
                    isTranscriptionError = !it.isTranscriptionError
                ) }
            }

            is InputTransactionEvent.SaveTransaction -> {
                viewModelScope.launch {
                    transactionDao.insertTransaction(state.value.proposedTransaction)
                    if (state.value.proposedTransaction.type == TransactionType.SPENDING) {
                        updateTotalExpense(state.value.proposedTransaction.total)
                    }
                    if (state.value.proposedTransaction.type == TransactionType.EARNING) {
                        updateTotalIncome(state.value.proposedTransaction.total)
                    }

                    _state.update { InputTransactionState() }
                }
            }

            InputTransactionEvent.UpdateTransaction -> {
                viewModelScope.launch {
                    transactionDao.updateTransaction(state.value.proposedTransaction)
                    if (state.value.proposedTransaction.type == TransactionType.SPENDING) {
                        setTotalOutcome(transactionDao.getTotalSpending())
                    }
                    if (state.value.proposedTransaction.type == TransactionType.EARNING) {
                        setTotalIncome(transactionDao.getTotalEarning())
                    }
                }

                _state.update { InputTransactionState() }
            }

            InputTransactionEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    transactionDao.deleteTransaction(state.value.proposedTransaction)
                    updateTotalExpense(-state.value.proposedTransaction.total)
                }
            }
        }
    }
}

private suspend fun manipulateDataStore(action: suspend () -> Unit, errorMessage: String) {
    try {
        action()
    } catch (e: Exception) {
        Log.e("WalletViewModel", errorMessage, e)
    }
}