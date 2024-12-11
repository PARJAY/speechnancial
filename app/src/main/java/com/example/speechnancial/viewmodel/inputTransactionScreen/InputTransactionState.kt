package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import android.content.Intent
import android.speech.SpeechRecognizer
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import com.example.speechnancial.data.model.Transaction

data class InputTransactionState(
    val context: Context,
    val recordAudioPermissionResultLauncher: ActivityResultLauncher<String>,
    val previousPartialResult: MutableState<String>,
    val source: MutableState<String>,
    val transaction: MutableState<Transaction>,
    val isTranscribing: MutableState<Boolean>,
    val isFinishedTranscribing: MutableState<Boolean>,
    val speechRecognizer: MutableState<SpeechRecognizer>,
    val speechRecognizerIntent: MutableState<Intent>
) {
    fun updateTransaction(update: Transaction.() -> Transaction) {
        transaction.value = transaction.value.update()
    }
}
