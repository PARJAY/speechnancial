package com.example.speechnancial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.speechnancial.data.db.AppDatabase
import com.example.speechnancial.ui.navigation.Navigation
import com.example.speechnancial.ui.theme.SpeechnancialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechnancialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                     Navigation(innerPadding)
                }
            }
        }
    }
}


