package com.example.speechnancial.viewmodel.transactionListScreen

import com.example.speechnancial.data.model.Transaction

// for previewing component
data class TransactionItemState(
    val transaction: Transaction = Transaction(),
    val isExpanded: Boolean = false
)

data class TransactionRoomItemState(
    val transaction: Transaction = Transaction(),
    val isExpanded: Boolean = false
)