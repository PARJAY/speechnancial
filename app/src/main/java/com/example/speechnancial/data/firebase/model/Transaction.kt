package com.example.speechnancial.data.firebase.model

import com.example.speechnancial.common.TransactionTypeOld
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Transaction(
    @DocumentId val uuid: String = "",
    val fullText: String = "",
    val details: Map<String, Float>? = null,
    val total: Float = 0.0f,
    val dateAdded: Timestamp = Timestamp.now(),
    val transactionTypeOldOrdinalOld: Int = TransactionTypeOld.UNDEFINED.ordinal,
    val transactionTypeOrdinal: Int = TransactionType.UNDEFINED.ordinal,
    val relatedEntityId: String? = null,
    val relatedWalletUuid: String? = null,
    val isValid: Boolean = true,
    val isNeedRevise: Boolean = false,
    val isFromSmartwatch: Boolean = false,
)


enum class TransactionType {
    UNDEFINED,
    EXPENSE,
    INCOME,
    REGULAR_TRANSFER,
    SAVING_DEPOSIT,
    SAVING_WITHDRAWAL,
    DEBT_PAYMENT,
    RECEIVABLE_PAYMENT
}