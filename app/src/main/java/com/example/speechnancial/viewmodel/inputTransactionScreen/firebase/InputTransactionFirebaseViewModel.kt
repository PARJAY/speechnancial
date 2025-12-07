package com.example.speechnancial.viewmodel.inputTransactionScreen.firebase

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.common.TRANSACTION_COLLECTION
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.tools.createTransactionFromInput
import com.example.speechnancial.tools.createTransactionWithBoyerMooreFromInput
import com.example.speechnancial.tools.startSpeechToText
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionEvent
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class InputTransactionFirebaseViewModel(
    private val firestore: FirebaseFirestore,
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

    fun onEvent(event : InputTransactionEvent) {
        when(event) {
            is InputTransactionEvent.IsEditExistingTransactionData -> {
                _state.update { it.copy(
                    proposedTransaction = event.transaction,
                    source = event.transaction.details?.entries?.joinToString(separator = "\n") { (key, value) ->
                        "$key ${value.toInt()} rupiah"
                    }?.ifEmpty { event.transaction.rawText }.toString(),
                    isEditExistingTransaction = true,
                    previousTransaction = event.transaction,
                    isReviseNeeded = event.transaction.isReviseNeeded,
                    isTranscriptionError = event.transaction.isTranscriptionError,
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
                Log.d("ITFVM", event.userInput)
                _state.update { it.copy(
                    source = event.userInput,
                    previousPartialResult = ""
                ) }
            }

            InputTransactionEvent.ResetInput -> _state.update { InputTransactionState() }

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
                finishedInputting()

                val transaction = state.value.proposedTransaction // Get the proposed transaction

                // 1. Generate the Firestore ID
                val docRef = firestore.collection(TRANSACTION_COLLECTION).document() // No ID specified
                val generatedId = docRef.id

                // 2. Create a *new* Transaction object with the generated ID
                val transactionWithId = transaction.copy(
                    id = generatedId,
                    isReviseNeeded = state.value.isReviseNeeded,
                    isTranscriptionError = state.value.isTranscriptionError
                )  // Use copy() to create a new object

                // 3. Add the transaction with the ID to Firestore
                docRef.set(transactionWithId) // Use docRef to set the data
                    .addOnSuccessListener {
                        Log.d("CTS : ", "success")

                        // Update the transaction in the local state with the generated ID
                        _state.update { currentState ->
                            currentState.copy(
                                proposedTransaction = transactionWithId // Update with the object including the ID
                            )
                        }

                        viewModelScope.launch {
                            if (transactionWithId.type == TransactionTypeOld.SPENDING) { // Use transactionWithId here
                                updateTotalExpense(transactionWithId.total)
                            }
                            if (transactionWithId.type == TransactionTypeOld.EARNING) { // And here
                                updateTotalIncome(transactionWithId.total)
                            }
                        }

                        _state.update { InputTransactionState() } // Reset the state
                    }
                    .addOnFailureListener { exception ->
                        Log.d("CTS : ", "failure $exception")
                    }
            }

            // todo : masih ada 1 hal yang bisa jadi patokan update,
            //  yaitu data total lama yang ada di transaction bisa jadi patokan
            //  jadi nggak perlu nyari total spending gini

            InputTransactionEvent.UpdateTransaction -> {
                finishedInputting()

                val oldTransaction = state.value.previousTransaction
                val newTransaction = state.value.proposedTransaction.copy(
                    isReviseNeeded = state.value.isReviseNeeded,
                    isTranscriptionError = state.value.isTranscriptionError
                )

                if (oldTransaction == newTransaction) {
                    Log.d("ITFVM", "data sama")
                    return
                }

                firestore.collection(TRANSACTION_COLLECTION)
                    .document(newTransaction.id).set(newTransaction)
                    .addOnSuccessListener {
                        Log.d("ITFVM", "Success updating data")
                        viewModelScope.launch {
                            when {
                                //  sebelumnya spending ato earning
                                oldTransaction.type == newTransaction.type -> {
                                    // tetep spending
                                    if (newTransaction.type == TransactionTypeOld.EARNING) {
                                        updateTotalIncome(newTransaction.total - oldTransaction.total)
                                    }
                                    // tetep earning
                                    else if (newTransaction.type == TransactionTypeOld.SPENDING) {
                                        updateTotalExpense(newTransaction.total - oldTransaction.total)
                                    }
                                }

                                //  sebelumnya spending jadi earning
                                oldTransaction.type == TransactionTypeOld.SPENDING && newTransaction.type == TransactionTypeOld.EARNING -> {
                                    updateTotalExpense(-oldTransaction.total)
                                    updateTotalIncome(newTransaction.total)
                                }
                                //  sebelumnya earning jadi spending
                                oldTransaction.type == TransactionTypeOld.EARNING && newTransaction.type == TransactionTypeOld.SPENDING -> {
                                    updateTotalIncome(-oldTransaction.total)
                                    updateTotalExpense(newTransaction.total)
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("ITFVM", "Fail to updating data $exception")
                    }

                _state.update { InputTransactionState() }
            }

            InputTransactionEvent.DeleteTransaction -> {
                firestore.collection(TRANSACTION_COLLECTION)
                    .document(state.value.proposedTransaction.id)
                    .delete()
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            updateTotalExpense(-state.value.proposedTransaction.total)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("ITFVM", "Fail to delete data $exception")
                    }
            }

            InputTransactionEvent.FinishInputing -> {
                Log.d("ITFVM", "FinishInputing running")

                finishedInputting()
            }
        }
    }

    private fun finishedInputting() {
        _state.update { it.copy(
//            proposedTransaction = createTransactionFromInput(
            proposedTransaction = createTransactionWithBoyerMooreFromInput(
                it.source.lowercase(),
                state.value.proposedTransaction.id
            )
        )}
    }
}

private suspend fun manipulateDataStore(action: suspend () -> Unit, errorMessage: String) {
    try {
        action()
    } catch (e: Exception) {
        Log.e("WalletViewModel", errorMessage, e)
    }
}

