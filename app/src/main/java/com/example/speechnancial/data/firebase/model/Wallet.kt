package com.example.speechnancial.data.firebase.model

import com.google.firebase.firestore.DocumentId

data class Wallet(
    @DocumentId val uuid: String = "",
    val name: String = "",
    var balance: Float = 0.0f,
    val isDefaultWallet: Boolean = false,
    var totalSpending: Float = 0.0f,
    var totalEarning: Float = 0.0f 
)