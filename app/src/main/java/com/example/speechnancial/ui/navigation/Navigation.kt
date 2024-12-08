package com.example.speechnancial.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.MyApp
import com.example.speechnancial.presentation.makeInputTransactionVM
import com.example.speechnancial.presentation.makeTransactionListVM
import com.example.speechnancial.tools.rememberSpeechToTransactionState
import com.example.speechnancial.ui.screen.InputTransactionScreen
import com.example.speechnancial.ui.screen.TransactionListScreen

@Composable
fun Navigation(innerPadding : PaddingValues) {

    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = InputTransactionScreenNavigation,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable<InputTransactionScreenNavigation> {
            val transactionVM = makeInputTransactionVM(MyApp.appModule.database)
            val state = rememberSpeechToTransactionState()

            InputTransactionScreen(navController, state, transactionVM::onEvent)
        }

        composable<TransactionListScreenNavigation> {
            val transactionListVM = makeTransactionListVM(MyApp.appModule.database)
            val state = transactionListVM.state.collectAsStateWithLifecycle()

            TransactionListScreen(state, transactionListVM::onEvent)
        }
    }
}