package com.example.speechnancial.data.firebase

import android.util.Log
import com.example.speechnancial.common.INTERNET_ISSUE
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.TransactionDetail
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.data.firebase.model.Keyword
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

// todo : untested yet
class FirebaseHelper {
    companion object {

        // revisi
//        data class Transaction(
//            @DocumentId val uuid: String = "",
//            val fullText: String = "",
//            val details: Map<String, Float>? = null,
//            val total: Float = 0.0f,
//            @ServerTimestamp val dateAdded: Timestamp = Timestamp.now(),
//            val transactionTypeOrdinal: Int = TransactionType.UNDEFINED.ordinal,
//            val transactionKindOrdinal: Int = TransactionKind.UNDEFINED.ordinal,
//            val relatedKindUuid: String? = null,
//            val relatedWalletUuid: String? = null,
//            val isValid: Boolean = true,
//            val isNeedRevise: Boolean = false,
//            val isFromSmartwatch: Boolean = false,
//        )

        fun fetchSnapshotToCustomerModel(queryDocumentSnapshot: DocumentSnapshot): UserModel {
            return UserModel(
                id = queryDocumentSnapshot.id,
                name = queryDocumentSnapshot.getString("name") ?: "",
                address = queryDocumentSnapshot.getString("address") ?: "",
                phone_number = queryDocumentSnapshot.getString("phone_number") ?: "",
                pair_code = queryDocumentSnapshot.getString("pair_code") ?: "" // ✅ tambahan
            )
        }

        fun fetchSnapshotToTransaction(queryDocumentSnapshot: DocumentSnapshot) =
            Transaction(
                uuid = queryDocumentSnapshot.id,
                fullText = queryDocumentSnapshot.getString("fullText") ?: "",
                details = queryDocumentSnapshot.data?.get("details") as? Map<String, Float>,
                total = queryDocumentSnapshot.getDouble("total")?.toFloat() ?: 0.0f, // Gunakan getDouble
                dateAdded = queryDocumentSnapshot.getTimestamp("dateAdded") ?: Timestamp.now(),
                transactionTypeOldOrdinalOld = queryDocumentSnapshot.getLong("transactionTypeOldOrdinalOld")?.toInt() ?: TransactionTypeOld.UNDEFINED.ordinal, //Perbaikan nama variabel
                transactionTypeOrdinal = queryDocumentSnapshot.getLong("transactionTypeOrdinal")?.toInt() ?: TransactionType.UNDEFINED.ordinal,
                relatedEntityId = queryDocumentSnapshot.getString("relatedEntityId"),

                relatedWalletUuid = queryDocumentSnapshot.getString("relatedWalletUuid"),
                isValid = queryDocumentSnapshot.getBoolean("valid") ?: true,
                isNeedRevise = queryDocumentSnapshot.getBoolean("needRevise") ?: false,
                isFromSmartwatch = queryDocumentSnapshot.getBoolean("fromSmartwatch") ?: false,
            )

        fun fetchSnapshotToDetailTransaction(queryDocumentSnapshot: DocumentSnapshot) : TransactionDetail {
            return TransactionDetail(
                uuid = queryDocumentSnapshot.id,
                description = queryDocumentSnapshot.getString("description") ?: "",
                amount = queryDocumentSnapshot.getLong("amount")?.toFloat() ?: 0.0f,
                kategoriuid = queryDocumentSnapshot.getString("kategoriuid") ?: ""
            )
        }

        fun fetchSnapshotToCategory(queryDocumentSnapshot: DocumentSnapshot) =
            Category(
                uuid = queryDocumentSnapshot.id,
                name = queryDocumentSnapshot.getString("name") ?: "",
                enumTransactionType = queryDocumentSnapshot.getLong("enumTransactionType")?.toInt() ?: EnumTransactionType.UNDEFINED.ordinal,
                isDeleted = queryDocumentSnapshot.getBoolean("isDeleted") ?: false,
            )

        fun fetchSnapshotToSaving(queryDocumentSnapshot: DocumentSnapshot) =
            Saving(
                uuid = queryDocumentSnapshot.id,
                name = queryDocumentSnapshot.getString("name") ?: "",
                collectedAmount = queryDocumentSnapshot.getDouble("collectedAmount")?.toFloat() ?: 0.0f,
                targetAmount = queryDocumentSnapshot.getDouble("targetAmount")?.toFloat() ?: 0.0f,
                savingTargetDate = queryDocumentSnapshot.getTimestamp("savingTargetDate") ?: Timestamp.now()
            )

        fun fetchSnapshotToBudget(queryDocumentSnapshot: DocumentSnapshot) =
            Budget(
                uuid = queryDocumentSnapshot.id,
                name = queryDocumentSnapshot.getString("name") ?: "",
                amount = queryDocumentSnapshot.getDouble("amount")?.toFloat() ?: 0.0f,
                startTime = queryDocumentSnapshot.getTimestamp("startTime") ?: Timestamp.now(),
                timeRangeInDays = queryDocumentSnapshot.getLong("timeRangeInDays")?.toInt() ?: 0,
                recurringTypeOrdinal = queryDocumentSnapshot.getLong("recurringTypeOrdinal")?.toInt()
                    ?: EnumTimeRange.NOT_RECURRING.ordinal,
                involvedCategoriesUuid = queryDocumentSnapshot.get("involvedCategoriesUuid") as? List<String>,
                transactionTypeOrdinal = queryDocumentSnapshot.getLong("transactionTypeOrdinal")?.toInt()
                    ?: EnumTransactionType.OUTCOME.ordinal,
                budgetRealizations = queryDocumentSnapshot.get("budgetRealizations")?.let {
                    (it as? List<Map<String, Any>>)?.map { map -> fetchMapToBudgetRealization(map) } ?: emptyList()
                } ?: emptyList(),
                isDeleted = queryDocumentSnapshot.getBoolean("isDeleted") ?: false
            )

        private fun fetchMapToBudgetRealization(map: Map<String, Any>) =
            BudgetRealization(
                progressAmount = (map["progressAmount"] as? Number)?.toFloat() ?: 0.0f,
                startTime = (map["startTime"] as? Timestamp) ?: Timestamp.now(),
                endTime = (map["endTime"] as? Timestamp) ?: Timestamp.now(),
                involvedTransactionsUuid = map["involvedTransactionsUuid"] as? List<String>,
                isDeleted = map["isDeleted"] as? Boolean ?: false
            )

//        fun fetchSnapshotToBudget(queryDocumentSnapshot: DocumentSnapshot) =
//            Budget(
//                uuid = queryDocumentSnapshot.id,
//                name = queryDocumentSnapshot.getString("name") ?: "",
//                amount = queryDocumentSnapshot.getDouble("amount")?.toFloat() ?: 0.0f,
//                startTime = queryDocumentSnapshot.getTimestamp("startTime") ?: Timestamp.now(),
//                timeRangeInDays = queryDocumentSnapshot.getLong("timeRangeInDays")?.toInt() ?: 0,
//                recurringTypeOrdinal = queryDocumentSnapshot.getLong("recurringTypeOrdinal")?.toInt()
//                    ?: EnumTimeRange.NOT_RECURRING.ordinal,
//                involvedCategoriesUuid = queryDocumentSnapshot.get("involvedCategoriesUuid") as? List<String>,
//                transactionTypeOrdinal = queryDocumentSnapshot.getLong("transactionTypeOrdinal")?.toInt()
//                    ?: EnumTransactionType.OUTCOME.ordinal,
//                // Mengubah cara mendapatkan budgetRealizations dari dokumen
//                budgetRealizations = queryDocumentSnapshot.get("budgetRealizations")?.let {
//                    // 1. Cast ke Map<String, Map<String, Any>>
//                    (it as? Map<String, Map<String, Any>>)?.mapValues { (_, value) ->
//                        // 2. Ubah setiap Map<String, Any> menjadi BudgetRealization
//                        fetchMapToBudgetRealization(value)
//                    } ?: emptyMap()
//                } ?: emptyMap(),
//                isDeleted = queryDocumentSnapshot.getBoolean("isDeleted") ?: false
//            )
//
//        private fun fetchMapToBudgetRealization(map: Map<String, Any>) =
//            BudgetRealization(
//                remainingAmount = (map["remainingAmount"] as? Number)?.toFloat() ?: 0.0f,
//                startTime = (map["startTime"] as? Timestamp) ?: Timestamp.now(),
//                endTime = (map["endTime"] as? Timestamp) ?: Timestamp.now(),
//                involvedTransactions = map["involvedTransactions"] as? List<String>,
//                isDeleted = map["isDeleted"] as? Boolean ?: false
//            )

        fun fetchSnapshotToWallet(queryDocumentSnapshot: DocumentSnapshot): Wallet {
            val data = queryDocumentSnapshot.data ?: emptyMap()

            return Wallet(
                uuid = queryDocumentSnapshot.id,
                name = data["name"] as? String ?: "",
                balance = (data["balance"] as? Number)?.toFloat() ?: 0.0f,
                totalSpending = (data["totalSpending"] as? Number)?.toFloat() ?: 0.0f,
                totalEarning = (data["totalEarning"] as? Number)?.toFloat() ?: 0.0f,
                isDefaultWallet = data["defaultWallet"] as? Boolean ?: false
            )
        }

        fun fetchSnapshotToKeyword(queryDocumentSnapshot: DocumentSnapshot): Keyword {
            return Keyword(
                uuid = queryDocumentSnapshot.id,
                key = queryDocumentSnapshot.getString("key") ?: "",
                relatedUsage = queryDocumentSnapshot.getString("relatedUsage") ?: "",
            )
        }

        fun fetchSnapshotToDebtAndReceivable(queryDocumentSnapshot: DocumentSnapshot) =
            DebtAndReceivable(
                uuid = queryDocumentSnapshot.id,
                name = queryDocumentSnapshot.getString("name") ?: "",
                amount = queryDocumentSnapshot.getDouble("amount")?.toFloat() ?: 0.0f,
                paidAmount = queryDocumentSnapshot.getDouble("paidAmount")?.toFloat() ?: 0.0f,
                dueDate = queryDocumentSnapshot.getTimestamp("dueDate") ?: Timestamp.now(),
                isPaid = queryDocumentSnapshot.getBoolean("paid") ?: false,
                note = queryDocumentSnapshot.getString("note") ?: "",
                typeOrdinal = queryDocumentSnapshot.getLong("typeOrdinal")?.toInt() ?: DebtType.DEBT.ordinal,
                isSoftDeleted = queryDocumentSnapshot.getBoolean("isSoftDeleted") ?: false
            )

        fun handleSnapshotListener(
            snapshot: QuerySnapshot?,
            exception: FirebaseFirestoreException?,
            errorCallback: (Exception) -> Unit,
            onDocumentChange: (DocumentChange) -> Unit
        ) {
            if (exception != null) {
                errorCallback(IllegalStateException(INTERNET_ISSUE))
                return
            }

            snapshot?.documentChanges?.forEach { change ->
                onDocumentChange(change)
                Log.d("Data Repo: ", "Data In -> ${change.type} - ${change.document}")
            }
        }
    }
}