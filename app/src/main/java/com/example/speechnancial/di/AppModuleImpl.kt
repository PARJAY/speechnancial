package com.example.speechnancial.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.speechnancial.data.db.AppDatabase

class AppModuleImpl(
    private val appContext: Context,
    private val application: Application
): AppModule {

    override val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "transaction.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}