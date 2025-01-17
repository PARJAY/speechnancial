package com.example.speechnancial

import android.app.Application
import com.example.speechnancial.di.AppModule
import com.example.speechnancial.di.AppModuleImpl

class MyApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this, this)
    }

}