package com.example.speechnancial.presentation.transactionListScreen

import com.example.speechnancial.data.model.Transaction

data class TransactionListState(
    val transactionList: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction = Transaction(),
    val showUpdateTransactionDialog: Boolean = false,
)