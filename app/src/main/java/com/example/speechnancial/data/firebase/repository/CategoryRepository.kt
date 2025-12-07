package com.example.speechnancial.data.firebase.repository

import com.example.speechnancial.common.CATEGORY_COLLECTION
import com.example.speechnancial.common.USER_COLLECTION
import com.example.speechnancial.data.firebase.FirebaseHelper
import com.example.speechnancial.data.firebase.FirebaseHelper.Companion.handleSnapshotListener
import com.example.speechnancial.data.firebase.model.Category
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class CategoryRepository(private val db: FirebaseFirestore) {
    private var listenerRegistration: ListenerRegistration? = null

    fun getCategoryList(
        userId: String,
        errorCallback: (Exception) -> Unit,
        addDataCallback: (Category) -> Unit,
        updateDataCallback: (Category) -> Unit,
        deleteDataCallback: (documentId: String) -> Unit
    ) {
        listenerRegistration = db.collection(USER_COLLECTION)
            .document(userId)
            .collection(CATEGORY_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotListener(snapshot, exception, errorCallback) { change ->
                    val categoryModel = FirebaseHelper.fetchSnapshotToCategory(change.document)
                    when (change.type) {
                        DocumentChange.Type.ADDED -> addDataCallback(categoryModel)
                        DocumentChange.Type.MODIFIED -> updateDataCallback(categoryModel)
                        DocumentChange.Type.REMOVED -> deleteDataCallback(change.document.id)
                    }
                }
            }
    }

    suspend fun addCategory(userId: String, category: Category, callback: (message: String) -> Unit) {
        try {
            val docRef = db.collection(USER_COLLECTION)
                .document(userId)
                .collection(CATEGORY_COLLECTION)
                .document()
            val newCategory = category.copy(uuid = docRef.id)
            docRef.set(newCategory).await()
            callback("Data Kategori Baru Berhasil Ditambahkan")
        } catch (e: Exception) {
            callback("Data Kategori Baru Gagal Ditambahkan: $e")
            println("Error adding category: ${e.message}")
        }
    }

    suspend fun updateCategory(userId: String, category: Category, callback: (message: String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(CATEGORY_COLLECTION)
                .document(category.uuid)
                .set(category)
                .await()
            callback("Data Kategori Berhasil Diperbaharui")
        } catch (e: Exception) {
            callback("Data Kategori Gagal Diperbaharui: $e")
            println("Error updating category: ${e.message}")
        }
    }

    suspend fun softDeleteCategory(userId: String, categoryId: String, callback: (message: String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(CATEGORY_COLLECTION)
                .document(categoryId)
                .update("isDeleted", true)
                .await()
            callback("Data Kategori Berhasil Dihapus")
        } catch (e: Exception) {
            callback("Data Kategori Gagal Dihapus: $e")
            println("Error deleting category: ${e.message}")
        }
    }

    suspend fun deleteCategory(userId: String, categoryId: String, callback: (message: String) -> Unit) {
        try {
            db.collection(USER_COLLECTION)
                .document(userId)
                .collection(CATEGORY_COLLECTION)
                .document(categoryId)
                .delete()
                .await()
            callback("Data Kategori Berhasil Dihapus Permanen")
        } catch (e: Exception) {
            callback("Data Kategori Gagal Dihapus Permanen: $e")
            println("Error deleting category: ${e.message}")
        }
    }
}