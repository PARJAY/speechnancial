package com.example.speechnancial.viewmodel.inputTransactionScreen

import android.content.Context
import android.content.Intent
import android.speech.SpeechRecognizer
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import com.example.speechnancial.data.model.Transaction

data class InputTransactionState(
    val previousPartialResult: String = "",
    val source: String = "",
    val proposedTransaction: Transaction = Transaction(),
    val isEditExistingTransaction : Boolean = false,
    val isTranscribing: Boolean = false,
    val isFinishedTranscribing: Boolean = true,
    val isReviseNeeded: Boolean = false,
    val isTranscriptionError: Boolean = false,
)