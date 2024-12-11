package com.example.speechnancial.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WalletBalanceAndHistory(
    val currentBalance: Float = 0f,
    val totalSpending: Float = 0f,
    val totalEarnings: Float = 0f,
) : Parcelable