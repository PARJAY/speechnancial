package com.example.speechnancial.newUi.screen.budgetDetailScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.common.budgetExample1BelanjaHarian
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.viewmodel.BudgetDetailHistoryEvent
import com.example.speechnancial.data.firebase.viewmodel.BudgetDetailHistoryUiState
import com.example.speechnancial.newUi.component.BudgetDetailHeaderItem
import com.example.speechnancial.newUi.component.TransactionItemWithCategory
import com.example.speechnancial.newUi.component.dialogBox.DialogBudgetCrud
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun BudgetHistoryDetailScreen(
    navController: NavHostController,
    budgetHistoryUiState: BudgetDetailHistoryUiState,
    onEvent: (BudgetDetailHistoryEvent) -> Unit,
    onBudgetDetailHistoryItemClick: (BudgetRealization) -> Unit
) {
    val context = LocalContext.current

    DialogBudgetCrud(
        showDialog = budgetHistoryUiState.showDialog,
        onDismiss = { onEvent(BudgetDetailHistoryEvent.HideDialog) },
        onSave = { },
        onUpdate = { updatedBudget ->
            onEvent(BudgetDetailHistoryEvent.EditBudget(updatedBudget, context))
        },
        onDelete = { budgetToDelete ->
            onEvent(BudgetDetailHistoryEvent.DeleteBudget(budgetToDelete, context, navController))
        },
        categories = budgetHistoryUiState.categories,
        selectedBudget = budgetHistoryUiState.selectedBudget
    )

    LazyColumn(
        Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
    ) {
        item {
            BudgetDetailHeaderItem(
                budgetHistoryUiState.selectedBudget,
                onClick = {
                    onEvent(BudgetDetailHistoryEvent.ShowDialog)
                }
            )

            Spacer(Modifier.height(16.dp))

            Text("History Anggaran Ini")
        }

        items(budgetHistoryUiState.transactions) { involvedTransaction ->
            TransactionItemWithCategory(
                transaction = involvedTransaction,
                category = budgetHistoryUiState.categories.find { category ->
                    category.uuid == involvedTransaction.relatedEntityId
                },
                onItemClick = {
                    // Todo : Handle item click
                    onBudgetDetailHistoryItemClick(budgetHistoryUiState.selectedBudgetRealization)
                },
            )
        }
    }
}


@PreviewLightDark
@Composable
fun BudgetHistoryDetailScreenPreview() {
    val navController = rememberNavController()

    val budgetHistoryUiState = BudgetDetailHistoryUiState(
        selectedBudget = budgetExample1BelanjaHarian,
        selectedBudgetRealization = BudgetRealization(),
        categories = emptyList(), // Anda bisa menambahkan data kategori jika perlu
        transactions = listOf(Transaction()),
        isLoading = false,
        errorMessage = null,
        showDialog = false,
        toastMessage = null,
    )

    SpeechnancialTheme {
        Surface (modifier = Modifier.fillMaxSize()) {
            BudgetHistoryDetailScreen(
                navController,
                budgetHistoryUiState = budgetHistoryUiState,
                onEvent = {}, // Anda bisa menambahkan logika event jika perlu
                onBudgetDetailHistoryItemClick = {}, // Anda bisa menambahkan logika klik item jika perlu,

            )
        }
    }
}