package com.example.speechnancial.ui.navigation

import com.example.speechnancial.common.TransactionType
import kotlinx.serialization.Serializable

@Serializable
object InputTransactionScreenNavigation

@Serializable
data class TransactionListScreenNavigation(
    val rawText: String = "",
    val type: TransactionType = TransactionType.UNDEFINED,
    val description: String = "",
    val nominal: Float? = null,
    val isTranscriptionCorrect: Boolean = true,
    val isNeedRevise: Boolean = false
)