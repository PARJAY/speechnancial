package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.Budget
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

private const val BUDGET_COLLECTION = "budgets"

class BudgetRepository(private val db: FirebaseFirestore) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getBudgetList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (Budget) -> Unit,
        updateDataCallback: (Budget) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(BUDGET_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val budgetModel = FirebaseHelper.fetchSnapshotToBudget(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(budgetModel)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(budgetModel)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    suspend fun addBudget(userId: String, budget: Budget, callback: (message: String) -> Unit) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(BUDGET_COLLECTION)
                .document()
            val newBudget = budget.copy(uuid = docRef.id)
            docRef.set(newBudget).await()
            callback("Data Budget Baru Berhasil Ditambahkan")
        } catch (e: Exception) {
            callback("Data Budget Baru Gagal Ditambahkan: ${e.message}")
            println("Error adding budget: ${e.message}")
        }
    }

    suspend fun updateBudget(userId: String, budget: Budget, callback: (message: String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(BUDGET_COLLECTION)
                .document(budget.uuid)
                .set(budget)
                .await()
            callback("Data Budget Berhasil Diperbaharui")
        } catch (e: Exception) {
            callback("Data Budget Gagal Diperbaharui: ${e.message}")
            println("Error updating budget: ${e.message}")
        }
    }

    suspend fun deleteBudget(userId: String, budgetId: String, callback: (message: String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(BUDGET_COLLECTION)
                .document(budgetId)
                .delete()
                .await()
            callback("Data Budget Berhasil Dihapus")
        } catch (e: Exception) {
            callback("Data Budget Gagal Dihapus: ${e.message}")
            println("Error deleting budget: ${e.message}")
        }
    }
}