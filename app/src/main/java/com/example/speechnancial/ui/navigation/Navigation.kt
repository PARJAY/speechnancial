package com.example.speechnancial.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.speechnancial.tools.rememberSpeechToTransactionState
import com.example.speechnancial.ui.screen.InputTransactionScreen
import com.example.speechnancial.ui.screen.TransactionListScreen

// todo : change to composable and make it
@Composable
fun Navigation(innerPadding :PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = InputTransactionScreenNavigation,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<InputTransactionScreenNavigation> {
            val state = rememberSpeechToTransactionState()
            InputTransactionScreen(navController, state)
        }
        composable<TransactionListScreenNavigation> {
            val args = it.toRoute<TransactionListScreenNavigation>()

            TransactionListScreen()
        }
    }
}