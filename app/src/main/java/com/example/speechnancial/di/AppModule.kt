package com.example.speechnancial.di

import com.example.speechnancial.data.db.AppDatabase

interface AppModule {
    val database: AppDatabase
}

