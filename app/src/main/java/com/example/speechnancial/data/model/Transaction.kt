package com.example.speechnancial.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.ui.navigation.TransactionListScreenNavigation
import kotlinx.parcelize.Parcelize

@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rawText: String = "",
    val type: TransactionType = TransactionType.UNDEFINED,
    val details: List<TransactionDetail>? = null,
    val createdAt: Long? = null,
    val isNeedRevise: Boolean = false,
    val isValid: Boolean = true
) : Parcelable {
    fun validator(): Boolean {
        if (type == TransactionType.UNDEFINED) return false
        return details?.all { it.emptyChecker() } ?: false
    }
}

@Parcelize
data class TransactionDetail(
    val description: String,
    val nominal: Float
) : Parcelable {
    fun emptyChecker() = description.isNotEmpty() && nominal > 0
}

// catatan :
// - jika ada kesalahan atau ketidak lengkapan dari transkripsi akan dimasukkan ke isTranscriptionCorrect = false
// - jika user tidak sengaja menginputkan kata-kata yang salah, akan dimasukkan ke isNeedRevise = true