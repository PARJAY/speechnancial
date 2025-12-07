package com.example.speechnancial.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.speechnancial.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY createdAtRoom DESC")
    fun getAllSortedTransactions(): Flow<List<Transaction>>

    @Query("""SELECT COALESCE(SUM(total), 0) FROM transactions WHERE type = 'SPENDING' """)
    suspend fun getTotalSpending(): Float

    @Query("""SELECT COALESCE(SUM(total), 0) FROM transactions WHERE type = 'EARNING' """)
    suspend fun getTotalEarning(): Float
}