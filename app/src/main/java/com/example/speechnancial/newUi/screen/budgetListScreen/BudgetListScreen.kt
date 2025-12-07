package com.example.speechnancial.newUi.screen.budgetListScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.common.budgetExample1BelanjaHarian
import com.example.speechnancial.common.budgetExample2GajianMagang
import com.example.speechnancial.common.budgetExample3Transportasi
import com.example.speechnancial.common.budgetExample4Thr
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.viewmodel.BudgetScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.BudgetScreenUiState
import com.example.speechnancial.newUi.component.BudgetItem
import com.example.speechnancial.newUi.component.dialogBox.DialogBudgetCrud
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun BudgetListScreen(
    budgetScreenUiState: BudgetScreenUiState,
    onEvent: (BudgetScreenEvent) -> Unit,
    onBudgetItemClick: (Budget) -> Unit
) {
    val context = LocalContext.current

    DialogBudgetCrud(
        showDialog = budgetScreenUiState.showDialogBudgetCrud,
        onDismiss = { onEvent(BudgetScreenEvent.CloseDialog) },
        onSave = { budget ->
            onEvent(BudgetScreenEvent.SaveBudget(budget, context))
        },
        onUpdate = { },
        onDelete = { },
        categories = budgetScreenUiState.categories,
        selectedBudget = budgetScreenUiState.selectedBudget
    )

    LazyColumn(Modifier.padding(top = 16.dp)) {
        item {
            Title("Daftar Budget")

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = budgetScreenUiState.searchQuery,
                onValueChange = { query ->
                    onEvent(BudgetScreenEvent.OnSearchQueryChange(query))
                },
                label = { Text("Cari budget...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )

            Spacer(Modifier.height(8.dp))
            Spacer(Modifier.height(8.dp))
        }

        items(budgetScreenUiState.budgets) { budget ->
            BudgetItem(
                budget,
                onClick = { onBudgetItemClick(budget) }
            )
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = { onEvent(BudgetScreenEvent.OpenDialog) },
        contentDescription = "Add Budget"
    )
}

@PreviewLightDark
@Composable
fun BudgetListScreenPreview() {
    SpeechnancialTheme {
        Surface {
            BudgetListScreen(
//                navController = rememberNavController(),
                budgetScreenUiState = BudgetScreenUiState(
                    budgets = listOf(
                        budgetExample1BelanjaHarian,
                        budgetExample2GajianMagang,
                        budgetExample3Transportasi,
                        budgetExample4Thr
                    )
                ),
                onEvent = {},
                onBudgetItemClick = {}
            )
        }
    }
}