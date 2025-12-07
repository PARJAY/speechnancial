package com.example.speechnancial.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class DebtAndReceivable(
    @DocumentId val uuid: String = "",
    val name: String = "",
    val amount: Float = 0f,
    val paidAmount: Float = 0f,
    val dueDate: Timestamp = Timestamp.now(),
    val isPaid: Boolean = false,
    val note: String = "",
    val typeOrdinal: Int = DebtType.DEBT.ordinal,
    val isSoftDeleted: Boolean = false

    // todo : val list involved transaction UUID and adjust firebase helper
)

enum class DebtType {
    DEBT,
    RECEIVABLE
}