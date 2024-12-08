package com.example.speechnancial.presentation.transactionListScreen

import com.example.speechnancial.data.model.Transaction

sealed interface TransactionListEvent {
    data class DeleteTransaction(val transaction : Transaction): TransactionListEvent
    data object UpdateSelectedTransaction: TransactionListEvent

    data class ShowDialog(val transaction : Transaction): TransactionListEvent
    data object HideDialog: TransactionListEvent
    data class HandleUserInput(val userInput : String): TransactionListEvent
}