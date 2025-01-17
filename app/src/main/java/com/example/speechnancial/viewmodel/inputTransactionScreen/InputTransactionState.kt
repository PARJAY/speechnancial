package com.example.speechnancial.viewmodel.inputTransactionScreen

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