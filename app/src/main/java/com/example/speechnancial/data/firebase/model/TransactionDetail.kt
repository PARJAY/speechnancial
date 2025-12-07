package com.example.speechnancial.data.firebase.model

import com.google.firebase.firestore.DocumentId

data class TransactionDetail(
    @DocumentId val uuid: String = "",
    val description: String = "",
    val amount: Float = 0.0f,
    val kategoriuid: String = "" // Reference to Kategori document
)
