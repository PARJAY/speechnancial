package com.example.speechnancial.viewmodel.inputTransactionScreen

sealed class SpeechToTransactionSideEffect {
    data class ShowSnackBarMessage(val message: String) : SpeechToTransactionSideEffect()
}