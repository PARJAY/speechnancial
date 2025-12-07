package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.Saving
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class SavingRepository(private val db: FirebaseFirestore) {

    private var listenerRegistration: ListenerRegistration? = null

    companion object {
        private const val SAVINGS_COLLECTION = "savings"
    }

    fun getSavingList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (Saving) -> Unit,
        updateDataCallback: (Saving) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(SAVINGS_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val model = FirebaseHelper.fetchSnapshotToSaving(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(model)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(model)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    suspend fun addSaving(
        userId: String,
        saving: Saving,
        callback: (String) -> Unit
    ) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(SAVINGS_COLLECTION)
                .document()
            val newSaving = saving.copy(uuid = docRef.id)
            docRef.set(newSaving).await()
            callback("Tabungan berhasil ditambahkan.")
        } catch (e: Exception) {
            callback("Gagal menambahkan tabungan: ${e.message}")
            println("Error adding saving: ${e.message}")
        }
    }

    suspend fun updateSaving(
        userId: String,
        saving: Saving,
        callback: (String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(SAVINGS_COLLECTION)
                .document(saving.uuid)
                .set(saving)
                .await()
            callback("Tabungan berhasil diperbarui.")
        } catch (e: Exception) {
            callback("Gagal memperbarui tabungan: ${e.message}")
            println("Error updating saving: ${e.message}")
        }
    }

    suspend fun deleteSaving(
        userId: String,
        savingId: String,
        callback: (String) -> Unit
    ) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(SAVINGS_COLLECTION)
                .document(savingId)
                .delete()
                .await()
            callback("Tabungan berhasil dihapus.")
        } catch (e: Exception) {
            callback("Gagal menghapus tabungan: ${e.message}")
            println("Error deleting saving: ${e.message}")
        }
    }
}