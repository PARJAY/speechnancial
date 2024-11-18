package com.example.speechnancial.ui.navigation

sealed class Screen(val route: String) {
    data object InputTransactionScreen : Screen("InputTransactionScreen")
    data object TransactionListScreen : Screen("TransactionListScreen")
}