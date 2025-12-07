package com.example.speechnancial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.speechnancial.newUi.navigation.MainScreen
import com.example.speechnancial.ui.theme.SpeechnancialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechnancialTheme {
                Surface {
                    MainScreen(lifecycleOwner = this) // Panggil MainScreen di sini
                }
            }
        }
    }
}