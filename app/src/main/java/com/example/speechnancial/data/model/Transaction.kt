package com.example.speechnancial.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.speechnancial.common.TransactionType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rawText: String = "",
    val type: TransactionType = TransactionType.UNDEFINED,
    val description: String = "",
    val nominal: Float? = null,
    // TODO : figure out later || error -> Cannot figure out how to save this field into database. You can consider adding a type converter for it.
    val dateAdded: String = "",
    val isTranscriptionCorrect: Boolean = true,
    val isNeedRevise: Boolean = false
)


// catatan :
// - jika ada kesalahan atau ketidak lengkapan dari transkripsi akan dimasukkan ke isTranscriptionCorrect = false
// - jika user tidak sengaja menginputkan kata-kata yang salah, akan dimasukkan ke isNeedRevise = true