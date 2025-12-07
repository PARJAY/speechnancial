package com.example.speechnancial.newUi.screen.budgetHistoryScreen

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
import com.example.speechnancial.data.firebase.viewmodel.BudgetHistoryEvent
import com.example.speechnancial.data.firebase.viewmodel.BudgetHistoryUiState
import com.example.speechnancial.newUi.component.BudgetHistoryHeaderItem
import com.example.speechnancial.newUi.component.BudgetHistoryItem
import com.example.speechnancial.newUi.component.dialogBox.DialogBudgetCrud
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun BudgetHistoryScreen(
    navController: NavHostController,
    budgetHistoryUiState: BudgetHistoryUiState,
    onEvent: (BudgetHistoryEvent) -> Unit,
    onBudgetHistoryItemClick: (BudgetRealization) -> Unit,
) {
    val context = LocalContext.current

    DialogBudgetCrud(
        showDialog = budgetHistoryUiState.showDialog,
        onDismiss = { onEvent(BudgetHistoryEvent.HideDialog) },
        onSave = {  },
        onUpdate = { updatedBudget ->
            onEvent(BudgetHistoryEvent.EditBudget(updatedBudget, context))
        },
        onDelete = { budgetToDelete ->
            onEvent(BudgetHistoryEvent.DeleteBudget(budgetToDelete, context, navController))
        },
        categories = budgetHistoryUiState.categories,
        selectedBudget = budgetHistoryUiState.selectedBudget
    )

    LazyColumn(
        Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    ) {
        item {
            BudgetHistoryHeaderItem(
                budgetHistoryUiState.selectedBudget,
                onClick = {
                    onEvent(BudgetHistoryEvent.ShowDialog)
                }
            )

            Spacer(Modifier.height(16.dp))

            Text("History Anggaran Ini")
        }

        items(budgetHistoryUiState.selectedBudget.budgetRealizations) { budgetRealization ->
            BudgetHistoryItem(
                budgetHistoryUiState.selectedBudget.transactionTypeOrdinal,
                budgetRealization,
                budgetHistoryUiState.selectedBudget.amount,
                onClick = {
                    onBudgetHistoryItemClick(budgetRealization)
                }
            )
        }
    }
}


@PreviewLightDark
@Composable
fun BudgetHistoryScreenPreview() {
    val navController = rememberNavController()

    val budgetHistoryUiState = BudgetHistoryUiState(
        selectedBudget = budgetExample1BelanjaHarian,
        categories = emptyList(), // Anda bisa menambahkan data kategori jika perlu
        isLoading = false,
        errorMessage = null,
        showDialog = false,
        toastMessage = null
    )

    SpeechnancialTheme {
        Surface (modifier = Modifier.fillMaxSize()) {
            BudgetHistoryScreen(
                navController,
                budgetHistoryUiState = budgetHistoryUiState,
                onEvent = {}, // Anda bisa menambahkan logika event jika perlu
                onBudgetHistoryItemClick = {} // Anda bisa menambahkan logika klik item jika perlu
            )
        }
    }
}