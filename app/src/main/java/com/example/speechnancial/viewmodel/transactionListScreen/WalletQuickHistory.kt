package com.example.speechnancial.viewmodel.transactionListScreen

import com.example.speechnancial.data.model.WalletBalanceAndHistory

data class WalletQuickHistory(
    val isFilterSpendingActive: Boolean = true,
    val isFilterEarningsActive: Boolean = true,
    val walletBalanceAndHistory: WalletBalanceAndHistory = WalletBalanceAndHistory()
)