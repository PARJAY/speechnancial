package com.example.speechnancial.newUi.navigation

import com.example.speechnancial.R
import kotlinx.serialization.Serializable

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    data object KeywordListScreen : BottomNavItem("KeywordListScreen", R.drawable.ic_category, "Keyword")
    data object CategoryListScreen : BottomNavItem("CategoryListScreen", R.drawable.ic_tag, "Kategori")
    data object TransactionListScreen : BottomNavItem("TransactionListScreen", R.drawable.ic_transaction, "Transaksi")
    data object WalletListScreen : BottomNavItem("WalletListScreen", R.drawable.ic_wallet, "Dompet")
    data object BudgetListScreen : BottomNavItem("BudgetListScreen", R.drawable.ic_budget_fit_2, "Anggaran")
    data object SavingListScreen : BottomNavItem("SavingListScreen", R.drawable.ic_saving_2, "Tabungan")
    data object DebtsAndReceivablesListScreen : BottomNavItem("DebtsAndReceivablesListScreen", R.drawable.ic_dept_receivable_2, "Hutang Piutang")
}

@Serializable
object LoginScreen

@Serializable
object SignUpScreen

@Serializable
object InputTransactionScreenNav

@Serializable
object CategoryDetailScreen

@Serializable
object WalletDetailScreen

@Serializable
object BudgetHistoryScreen

@Serializable
object BudgetHistoryDetailScreen

@Serializable
object SavingDetailScreen
