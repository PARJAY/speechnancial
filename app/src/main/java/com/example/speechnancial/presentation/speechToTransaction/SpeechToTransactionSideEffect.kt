package com.example.speechnancial.presentation.speechToTransaction

sealed class SpeechToTransactionSideEffect {
    data class ShowSnackBarMessage(val message: String) : SpeechToTransactionSideEffect()
}