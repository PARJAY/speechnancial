package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

const val DEBT_AND_RECEIVABLE_COLLECTION = "debtAndReceivables"

class DebtAndReceivableRepository(private val db: FirebaseFirestore) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getDebtAndReceivableList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (DebtAndReceivable) -> Unit,
        updateDataCallback: (DebtAndReceivable) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(DEBT_AND_RECEIVABLE_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val model = FirebaseHelper.fetchSnapshotToDebtAndReceivable(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(model)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(model)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    suspend fun addDebtAndReceivable(
        userId: String,
        debtAndReceivable: DebtAndReceivable,
        callback: (message: String) -> Unit
    ) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(DEBT_AND_RECEIVABLE_COLLECTION)
                .document()
            val newData = debtAndReceivable.copy(uuid = docRef.id)
            docRef.set(newData).await()
            callback("Data Hutang/Piutang Baru Berhasil Ditambahkan")
        } catch (e: Exception) {
            callback("Data Hutang/Piutang Baru Gagal Ditambahkan: $e")
            println("Error adding: ${e.message}")
        }
    }

    suspend fun updateDebtAndReceivable(
        userId: String,
        debtAndReceivable: DebtAndReceivable,
        callback: (message: String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(DEBT_AND_RECEIVABLE_COLLECTION)
                .document(debtAndReceivable.uuid)
                .set(debtAndReceivable)
                .await()
            callback("Data Hutang/Piutang Berhasil Diperbaharui")
        } catch (e: Exception) {
            callback("Data Hutang/Piutang Gagal Diperbaharui: $e")
            println("Error updating: ${e.message}")
        }
    }

    suspend fun softDeleteDebtAndReceivable(
        userId: String,
        debtAndReceivableId: String,
        callback: (message: String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(DEBT_AND_RECEIVABLE_COLLECTION)
                .document(debtAndReceivableId)
                .update("isSoftDeleted", true)
                .await()
            callback("Data Hutang/Piutang Berhasil Dihapus")
        } catch (e: Exception) {
            callback("Data Hutang/Piutang Gagal Dihapus: $e")
            println("Error soft delete: ${e.message}")
        }
    }

    suspend fun deleteDebtAndReceivable(
        userId: String,
        debtAndReceivableId: String,
        callback: (message: String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(DEBT_AND_RECEIVABLE_COLLECTION)
                .document(debtAndReceivableId)
                .delete()
                .await()
            callback("Data Hutang/Piutang Berhasil Dihapus")
        } catch (e: Exception) {
            callback("Data Hutang/Piutang Gagal Dihapus: $e")
            println("Error delete: ${e.message}")
        }
    }
}