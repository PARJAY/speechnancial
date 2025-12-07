package com.example.speechnancial.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.speechnancial.common.TransactionTypeOld
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,       // rubah jadi string
    @PrimaryKey val id: String = "",       // rubah jadi string
    val rawText: String = "",
    val type: TransactionTypeOld = TransactionTypeOld.UNDEFINED,
    val detailsRoom: List<TransactionDetail>? = null,
    val details: Map<String, Float>? = null,
    val total: Float = 0f,
    val createdAtRoom: LocalDateTime? = null,
    val createdAt: Timestamp? = null,
    val isReviseNeeded: Boolean = false,
    val isTranscriptionError: Boolean = false,
    val isValid: Boolean = true,
    val isFromSmartwatch : Boolean = false
) : Parcelable

@Parcelize
data class TransactionDetail(
    val description: String = "",
    val nominal: Float = 0f
) : Parcelable {
    fun emptyChecker() = description.isNotEmpty() && nominal > 0
}


