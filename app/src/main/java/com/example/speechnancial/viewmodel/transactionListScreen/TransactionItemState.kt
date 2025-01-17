package com.example.speechnancial.viewmodel.transactionListScreen

import com.example.speechnancial.data.model.Transaction

data class TransactionItemState(
    val transaction: Transaction = Transaction(),
    val isExpanded: Boolean = false
)