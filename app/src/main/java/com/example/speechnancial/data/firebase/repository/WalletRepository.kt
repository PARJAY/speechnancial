package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.common.WALLETS_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.Wallet
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class WalletRepository(private val db: FirebaseFirestore) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getWalletList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (Wallet) -> Unit,
        updateDataCallback: (Wallet) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(WALLETS_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val dompetModel = FirebaseHelper.fetchSnapshotToWallet(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(dompetModel)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(dompetModel)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    // gunakan callback untuk meneruskan respon firestore
    suspend fun addWallet(userId: String, wallet: Wallet, callback: (String) -> Unit) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(WALLETS_COLLECTION)
                .document()
            val newWallet = wallet.copy(uuid = docRef.id)
            docRef.set(newWallet).await()
            callback("Dompet berhasil ditambahkan.")
        } catch (e: Exception) {
            callback("Gagal menambahkan dompet: ${e.message}")
        }
    }

    suspend fun updateWallet(userId: String, wallet: Wallet, callback: (String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(WALLETS_COLLECTION)
                .document(wallet.uuid)
                .set(wallet)
                .await()
            callback("Dompet berhasil diperbarui.")
        } catch (e: Exception) {
            callback("Gagal memperbarui dompet: ${e.message}")
        }
    }

    suspend fun deleteWallet(userId: String, walletId: String, callback: (String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(WALLETS_COLLECTION)
                .document(walletId)
                .delete()
                .await()
            callback("Dompet berhasil dihapus.")
        } catch (e: Exception) {
            callback("Gagal menghapus dompet: ${e.message}")
        }
    }
}