package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.tools.createTransactionFromInput
import com.example.speechnancial.tools.startSpeechToText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class InputTransactionViewModel(
    private val transactionDao: TransactionDao,
    context: Context
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

    fun onEvent(event : InputTransactionEvent) {
        when(event) {
            is InputTransactionEvent.IsEditExistingTransactionData -> {
                _state.update { it.copy(
                    proposedTransaction = event.transaction,
                    isEditExistingTransaction = true
                ) }
            }

            is InputTransactionEvent.IsCloseScreen ->
                _state.update { it.copy(
                    isEditExistingTransaction = false
                ) }

            is InputTransactionEvent.SpeechToTransactionButtonClicked -> {
                // todo : unchecked
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

                Log.d("InputTransactionVMV : ", "state change")

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
                        proposedTransaction = createTransactionFromInput(
                            (it.source + it.previousPartialResult).lowercase()
                        )
                    )}
                }
            }

            is InputTransactionEvent.HandleUserInput -> {
                _state.update { it.copy(
                    source = event.userInput
                ) }
            }
            InputTransactionEvent.ResetButtonClicked -> {
                _state.update { it.copy(
                    source = ""
                ) }
            }

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
                }
            }
            InputTransactionEvent.UpdateTransaction -> {
                viewModelScope.launch {
                    transactionDao.updateTransaction(state.value.proposedTransaction)
                }

                _state.update { it.copy(
                    isEditExistingTransaction = false
                ) }
            }
            InputTransactionEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    transactionDao.deleteTransaction(state.value.proposedTransaction)
                }
            }
        }
    }
}




//if (!state.isTranscribing.value) {
//    state.isTranscribing.value = true
//    state.isFinishedTranscribing.value = false
//    startSpeechToText(
//        state.context,
//        state.speechRecognizer.value,
//        state.speechRecognizerIntent.value,
//        onPartialResults = {
//            if (state.previousPartialResult.value.isNotEmpty() && it.isEmpty()) {
//                state.source.value += state.previousPartialResult.value
////                    Log.d("source", state.source.value)
//            }
//
//            state.previousPartialResult.value = it
////                Log.d("previousPartialResult", it)
//        }
//    )
//    state.speechRecognizer.value.startListening(state.speechRecognizerIntent.value)
//} else {
//    state.isTranscribing.value = false
//    state.isFinishedTranscribing.value = true
//    state.speechRecognizer.value.stopListening()
//    state.speechRecognizer.value.destroy()
//
//    createTransactionFromInput(
//        (state.source.value + state.previousPartialResult.value).lowercase(),
//        state.transaction
//    )
//}