package com.example.speechnancial.presentation.transaction

sealed class TransactionSideEffect {
    data class ShowSnackBarMessage(val message: String) : TransactionSideEffect()
}