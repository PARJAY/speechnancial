package com.example.speechnancial.viewmodel.transactionListScreen

import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.WalletBalanceAndHistory

data class TransactionListState(
    val transactionList: List<TransactionItemState> = emptyList(),
    val isFilterSpendingActive: Boolean = true,
    val isFilterEarningActive: Boolean = true,
    val walletBalanceAndHistory: WalletBalanceAndHistory = WalletBalanceAndHistory(),
    val selectedTransaction: Transaction = Transaction(),
    val showUpdateTransactionDialog: Boolean = false,
)

data class TransactionItemState(
    val transaction: Transaction = Transaction(),
    val isExpanded: Boolean = false
)