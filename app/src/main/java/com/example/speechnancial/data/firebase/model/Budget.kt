package com.example.speechnancial.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

// todo : balance -> change to amount watch for error
// todo : the 3 last items is just added added here watch for error

//{
//    fun makeBudgetRealization() {
//        // if create is performed
//        // if not recurring updateOrCreate involvedBudgetRealization
//        // if recurring delete all involvedBudgetRealization and remake it again
//        // calculate startTime with today and make involvedBudgetRealization every timeRangeInDays
//        // last, make recurring handler
//
//        // if edit is performed
//        // if not recurring updateOrCreate involvedBudgetRealization
//        // if recurring delete all involvedBudgetRealization and remake it again
//        // calculate startTime with today and make involvedBudgetRealization every timeRangeInDays
//        // last, make recurring handler
//
//        // recurring handler
//        // easy : when the app open, calculate the latest BudgetRealization in this, and make BudgetRealization with the given time range
//        // ideal : there will be a background task scheduled to make the BudgetRealization
//    }
//}

data class Budget(
    @DocumentId val uuid: String = "",
    val name: String = "",
    var amount: Float = 0.0f,
    val startTime: Timestamp = Timestamp.now(),
    val timeRangeInDays: Int = 0,
    val recurringTypeOrdinal: Int = EnumTimeRange.NOT_RECURRING.ordinal,
    val involvedCategoriesUuid: List<String>? = null,
    val transactionTypeOrdinal: Int = EnumTransactionType.OUTCOME.ordinal,
    val budgetRealizations: List<BudgetRealization> = listOf(),
    val isDeleted: Boolean = false
)

data class BudgetRealization(
    @DocumentId val uuid: String = "",
    var progressAmount: Float = 0.0f,
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val involvedTransactionsUuid: List<String>? = listOf(),
    val isDeleted: Boolean = false
)

// todo :
//  get involved transaction
//  get involved wallet
