package com.example.speechnancial.viewmodel.transactionListScreen

import android.content.Context
import com.example.speechnancial.data.model.Transaction

sealed interface TransactionListEvent {
    data object FilterSpendingButtonClick: TransactionListEvent
    data object FilterEarningButtonClick: TransactionListEvent

    data class SettingButtonClick(val context: Context): TransactionListEvent
}