package com.example.speechnancial.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionConverter

@TypeConverters(TransactionConverter::class)
@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}