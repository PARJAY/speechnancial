package com.example.speechnancial.data.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Saving(
    @DocumentId val uuid: String = "",
    val name: String = "",
    val collectedAmount: Float = 0f, // Jumlah terkumpul
    val targetAmount: Float = 0f,    // Jumlah target
    val savingTargetDate: Timestamp = Timestamp.now()

    // todo : val list involved transaction UUID and adjust firebase helper
)