package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.TRANSACTION_DETAIL_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.TransactionDetail
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class TransactionDetailRepository(private val db: FirebaseFirestore) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getTransactionDetailList(
        errorCallback: (Exception) -> Unit,
        addDataCallback: (TransactionDetail) -> Unit,
        updateDataCallback: (TransactionDetail) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(TRANSACTION_DETAIL_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                 handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val detailTransactionModel = FirebaseHelper.fetchSnapshotToDetailTransaction(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(detailTransactionModel)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(detailTransactionModel)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    // todo : callback is not yet implemented
    suspend fun addTransactionDetail(transactionDetail: TransactionDetail, callback: () -> Unit) {
        try {
            val docRef = db.collection(TRANSACTION_DETAIL_COLLECTION).document() // Generate new ID
            val newTransactionDetail = transactionDetail.copy(uuid = docRef.id) // Set the ID
            docRef.set(newTransactionDetail).await()
            callback() // Invoke callback on success
        } catch (e: Exception) {
            // Handle error (e.g., log, callback with error)
            println("Error adding transactionDetail: ${e.message}")
        }
    }


    suspend fun updateTransactionDetail(transactionDetail: TransactionDetail, callback: () -> Unit) {
        try {
            db.collection(TRANSACTION_DETAIL_COLLECTION).document(transactionDetail.uuid)
                .set(transactionDetail).await() // Update existing document
            callback()
        } catch (e: Exception) {
            println("Error updating transactionDetail: ${e.message}")
        }
    }
}