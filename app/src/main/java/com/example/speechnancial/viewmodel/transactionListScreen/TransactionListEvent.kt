package com.example.speechnancial.viewmodel.transactionListScreen

import com.example.speechnancial.data.model.Transaction

sealed interface TransactionListEvent {
    data class TransactionItemOnClickShowDialog(val transaction : Transaction): TransactionListEvent
    data class HandleUserInput(val userInput : String): TransactionListEvent
    data object DialogActionDeleteTransaction: TransactionListEvent
    data object DialogActionUpdateSelectedTransaction: TransactionListEvent
    data object HideDialog: TransactionListEvent

    data object FilterSpendingButtonClick: TransactionListEvent
    data object FilterEarningButtonClick: TransactionListEvent

    data object SettingButtonClick: TransactionListEvent
}