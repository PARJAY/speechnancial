package com.example.speechnancial.data.firebase.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId val uuid: String = "",
    val name: String = "",
    val enumTransactionType: Int = EnumTransactionType.UNDEFINED.ordinal,
    val isDeleted: Boolean = false
)