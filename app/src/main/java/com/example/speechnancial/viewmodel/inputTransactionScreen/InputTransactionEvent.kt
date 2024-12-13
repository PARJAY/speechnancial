package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import com.example.speechnancial.data.model.Transaction

sealed interface InputTransactionEvent {
    data class IsEditExistingTransactionData(val transaction : Transaction): InputTransactionEvent
    data object IsCloseScreen: InputTransactionEvent

    data class SpeechToTransactionButtonClicked(val context: Context): InputTransactionEvent
    data object ResetButtonClicked: InputTransactionEvent

    data class HandleUserInput(val userInput: String): InputTransactionEvent

    data object ReviseLaterCheckboxClicked: InputTransactionEvent
    data object TranscriptionErrorCheckboxClicked: InputTransactionEvent

    data object SaveTransaction: InputTransactionEvent
    data object UpdateTransaction: InputTransactionEvent
    data object DeleteTransaction: InputTransactionEvent
}