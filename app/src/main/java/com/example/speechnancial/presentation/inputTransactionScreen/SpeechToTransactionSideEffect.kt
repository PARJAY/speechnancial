package com.example.speechnancial.presentation.inputTransactionScreen

sealed class SpeechToTransactionSideEffect {
    data class ShowSnackBarMessage(val message: String) : SpeechToTransactionSideEffect()
}