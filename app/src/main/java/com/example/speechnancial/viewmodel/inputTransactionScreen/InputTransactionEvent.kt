package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import androidx.navigation.NavHostController
import com.example.speechnancial.data.model.Transaction

sealed interface InputTransactionEvent {
    data class IsEditExistingTransactionData(val transaction : Transaction): InputTransactionEvent
    data class IsCloseScreen(val navController: NavHostController): InputTransactionEvent

    data class SpeechToTransactionButtonClicked(val context: Context): InputTransactionEvent
    data object ResetInput: InputTransactionEvent
    data class HandleUserInput(val userInput: String): InputTransactionEvent
    data object FinishInputing: InputTransactionEvent

    data object ReviseLaterCheckboxClicked: InputTransactionEvent
    data object TranscriptionErrorCheckboxClicked: InputTransactionEvent

    data object SaveTransaction: InputTransactionEvent
    data object UpdateTransaction: InputTransactionEvent
    data object DeleteTransaction: InputTransactionEvent
}