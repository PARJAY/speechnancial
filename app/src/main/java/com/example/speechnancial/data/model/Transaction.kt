package com.example.speechnancial.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.speechnancial.common.TransactionType
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rawText: String = "",
    val type: TransactionType = TransactionType.UNDEFINED,
    val details: List<TransactionDetail>? = null,
    val total : Float = 0f,
    val createdAt: LocalDateTime? = null,
    val isNeedRevise: Boolean = false,
    val transcriptionError: Boolean = false,
    val isValid: Boolean = true
) : Parcelable {
    fun validator(): Boolean {
        if (type == TransactionType.UNDEFINED) return false
        return details?.all { it.emptyChecker() } ?: false
    }
}

@Parcelize
data class TransactionDetail(
    val description: String = "",
    val nominal: Float = 0f
) : Parcelable {
    fun emptyChecker() = description.isNotEmpty() && nominal > 0
}