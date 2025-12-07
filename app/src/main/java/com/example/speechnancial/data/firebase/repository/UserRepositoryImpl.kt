package com.example.speechnancial.data.firebase.repository

import android.util.Log
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.fetchSnapshotToCustomerModel
import com.example.speechnancial.data.firebase.model.EditedUserModel
import com.example.speechnancial.data.firebase.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val USER_COLLECTION = "User"

class UserRepositoryImpl(private val db : FirebaseFirestore) {

    suspend fun getCustomerById(customerId: String): UserModel {
        val documentSnapshot = db.collection(USER_COLLECTION).document(customerId).get().await()
        Log.d("Customer Repo", "${documentSnapshot.data}")

        return fetchSnapshotToCustomerModel(documentSnapshot)
    }

    suspend fun addOrUpdateCustomer(customerId: String, newCustomer: UserModel) {
        db.collection(USER_COLLECTION).document(customerId).set(newCustomer).await()
    }

    // full update
    suspend fun updateCustomer(customerId: String, newCustomer: EditedUserModel) {
        db.collection(USER_COLLECTION).document(customerId).set(newCustomer).await()
    }

    // khusus update pair_code
    suspend fun updatePairCode(customerId: String, newPairCode: String) {
        db.collection(USER_COLLECTION).document(customerId)
            .update("pair_code", newPairCode)
            .await()
    }

    fun isPairCodeAvailable(pairCode: String, currentUserId: String, callback: (Boolean) -> Unit) {
        db.collection("User")
            .whereEqualTo("pairCode", pairCode)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Pair code belum dipakai siapa pun
                    callback(true)
                } else {
                    // Ada dokumen, cek apakah itu user yang sama
                    val existingUser = querySnapshot.documents[0]
                    val existingUserId = existingUser.getString("id") ?: existingUser.id
                    callback(existingUserId == currentUserId) // true kalau dipakai dirinya sendiri
                }
            }
            .addOnFailureListener {
                callback(false) // fallback gagal
            }
    }
}