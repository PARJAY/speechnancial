package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.TRANSACTION_COLLECTION
import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.Transaction
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

//interface ITransactionRepository {
//    suspend fun addTransactionOld(transaction: Transaction, callback: (String) -> Unit)
//    suspend fun updateTransaction(transaction: Transaction, callback: (String) -> Unit)
//    suspend fun deleteTransaction(uuid: String, callback: (String) -> Unit)
//}
//
//// tambahkan parameter id user dan folder id user disetiap CRUD
//open class TransactionRepository(private val db: FirebaseFirestore) : ITransactionRepository {
//    private var listenerRegistration: ListenerRegistration? = null
//
//    fun getTransactionList(
//        userId: String,
//        errorCallback: (Exception) -> Unit,
//        addDataCallback: (Transaction) -> Unit,
//        updateDataCallback: (Transaction) -> Unit,
//        deleteDataCallback: (documentId: String) -> Unit
//    ) {
//        listenerRegistration = db.collection(USER_COLLECTION)
//            .document(userId)
//            .collection(TRANSACTION_COLLECTION)
//            .addSnapshotListener { snapshot, exception ->
//                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
//                    val transactionModel = FirebaseHelper.fetchSnapshotToTransaction(change.document)
//                    when (change.type) {
//                        DocumentChange.Type.ADDED -> addDataCallback(transactionModel)
//                        DocumentChange.Type.MODIFIED -> updateDataCallback(transactionModel)
//                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
//                    }
//                }
//            }
//    }
//
//    override suspend fun addTransactionOld(transaction: Transaction, callback: (message: String) -> Unit) {
//        try {
//            val docRef = db.collection(TRANSACTION_COLLECTION).document()
//            val newTransaction = transaction.copy(uuid = docRef.id)
//            docRef.set(newTransaction).await()
//            callback("Transaksi Berhasil Ditambahkan")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Ditambahkan: ${e.message}"
//            println(errorMessage)
//            callback(errorMessage)
//        }
//    }
//
//    suspend fun addTransaction(userId: String, transaction: Transaction, callback: (Transaction, String) -> Unit) {
//        try {
//            val docRef = db.collection(USER_COLLECTION)
//                .document(userId)
//                .collection(TRANSACTION_COLLECTION)
//                .document()
//            val newTransaction = transaction.copy(uuid = docRef.id)
//            docRef.set(newTransaction).await()
//            callback(newTransaction, "Transaksi Berhasil Ditambahkan")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Ditambahkan: ${e.message}"
//            println(errorMessage)
//            callback(transaction, errorMessage) // tetap kembalikan original jika gagal
//        }
//    }
//
//    suspend fun updateTransaction(transaction: Transaction, callback: (Transaction, String) -> Unit) {
//        try {
//            db.collection(TRANSACTION_COLLECTION).document(transaction.uuid)
//                .set(transaction)
//                .await()
//            callback(transaction, "Transaksi Berhasil Ditambahkan")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Diperbaharui: ${e.message}"
//            println(errorMessage)
//            callback(transaction, errorMessage)
//        }
//    }
//
//    override suspend fun updateTransaction(transaction: Transaction, callback: (message: String) -> Unit) {
//        try {
//            db.collection(TRANSACTION_COLLECTION).document(transaction.uuid)
//                .set(transaction).await()
//            callback("Transaksi Berhasil Diperbaharui")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Diperbaharui: ${e.message}"
//            println(errorMessage)
//            callback(errorMessage)
//        }
//    }
//
//    suspend fun updateTransaction(
//        userId: String,
//        transaction: Transaction,
//        callback: (Transaction, String) -> Unit
//    ) {
//        try {
//            db.collection(USER_COLLECTION)
//                .document(userId)
//                .collection(TRANSACTION_COLLECTION)
//                .document(transaction.uuid)
//                .set(transaction)
//                .await()
//            callback(transaction, "Transaksi Berhasil Diperbaharui")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Diperbaharui: ${e.message}"
//            println(errorMessage)
//            callback(transaction, errorMessage)
//        }
//    }
//
//    suspend fun softDeleteTransaction(
//        userId: String,
//        transactionId: String,
//        callback: (message: String) -> Unit
//    ) {
//        try {
//            db.collection(USER_COLLECTION)
//                .document(userId)
//                .collection(TRANSACTION_COLLECTION)
//                .document(transactionId)
//                .update("isDeleted", true)
//                .await()
//            callback("Transaksi Berhasil Dihapus")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Dihapus: ${e.message}"
//            println(errorMessage)
//            callback(errorMessage)
//        }
//    }
//
//    override suspend fun deleteTransaction(transactionId: String, callback: (message: String) -> Unit) {
//        try {
//            db.collection(TRANSACTION_COLLECTION).document(transactionId).delete().await()
//            callback("Transaksi Berhasil Dihapus")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Dihapus: ${e.message}"
//            println(errorMessage)
//            callback(errorMessage)
//        }
//    }
//
//    suspend fun deleteTransaction(
//        userId: String,
//        transactionId: String,
//        callback: (message: String) -> Unit
//    ) {
//        try {
//            db.collection(USER_COLLECTION)
//                .document(userId)
//                .collection(TRANSACTION_COLLECTION)
//                .document(transactionId)
//                .delete()
//                .await()
//            callback("Transaksi Berhasil Dihapus")
//        } catch (e: Exception) {
//            val errorMessage = "Transaksi Gagal Dihapus: ${e.message}"
//            println(errorMessage)
//            callback(errorMessage)
//        }
//    }
//
//    fun removeListener() {
//        listenerRegistration?.remove()
//        listenerRegistration = null
//    }
//}


interface ITransactionRepository {
    suspend fun addTransaction(userId: String, transaction: Transaction, callback: (Transaction, String) -> Unit)
    suspend fun updateTransaction(userId: String, transaction: Transaction, callback: (Transaction, String) -> Unit)
    suspend fun deleteTransaction(userId: String, transactionId: String, callback: (String) -> Unit)
}

class TransactionRepository(private val db: FirebaseFirestore) {

    private var listenerRegistration: ListenerRegistration? = null

    fun getTransactionList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (Transaction) -> Unit,
        updateDataCallback: (Transaction) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(TRANSACTION_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val transactionModel = FirebaseHelper.fetchSnapshotToTransaction(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(transactionModel)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(transactionModel)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    suspend fun addTransaction(
        userId: String,
        transaction: Transaction,
        callback: (Transaction, String) -> Unit
    ) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(TRANSACTION_COLLECTION)
                .document()

            val newTransaction = transaction.copy(uuid = docRef.id)
            docRef.set(newTransaction).await()
            callback(newTransaction, "Transaksi Berhasil Ditambahkan")
        } catch (e: Exception) {
            callback(transaction, "Transaksi Gagal Ditambahkan: ${e.message}")
        }
    }

    suspend fun updateTransaction(
        userId: String,
        transaction: Transaction,
        callback: (Transaction, String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(TRANSACTION_COLLECTION)
                .document(transaction.uuid)
                .set(transaction)
                .await()
            callback(transaction, "Transaksi Berhasil Diperbaharui")
        } catch (e: Exception) {
            callback(transaction, "Transaksi Gagal Diperbaharui: ${e.message}")
        }
    }

    suspend fun deleteTransaction(
        userId: String,
        transactionId: String,
        callback: (message: String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(TRANSACTION_COLLECTION)
                .document(transactionId)
                .delete()
                .await()
            callback("Transaksi Berhasil Dihapus (Hard Delete)")
        } catch (e: Exception) {
            callback("Transaksi Gagal Dihapus: ${e.message}")
        }
    }
}