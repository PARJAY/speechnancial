package com.example.speechnancial.newUi.screen.debtsAndReceivablesListScreen

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
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.data.firebase.viewmodel.DebtAndReceivableScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.DebtAndReceivableScreenUiState
import com.example.speechnancial.newUi.component.ItemWithProgress
import com.example.speechnancial.newUi.component.dialogBox.DialogDebtAndReceivableCRUD
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp

@Composable
fun DebtsAndReceivablesListScreen(
    uiState: DebtAndReceivableScreenUiState,
    onEvent: (DebtAndReceivableScreenEvent) -> Unit
) {
    val context = LocalContext.current

    DialogDebtAndReceivableCRUD(
        showDialog = uiState.showDialogDebtAndReceivableCrud,
        selectedDebtAndReceivable = uiState.selectedDebtAndReceivable,
        onDismiss = { onEvent(DebtAndReceivableScreenEvent.CloseDialog) },
        onSave = { debtAndReceivable ->
            onEvent(DebtAndReceivableScreenEvent.SaveDebtAndReceivable(debtAndReceivable, context))
        },
        onUpdate = { debtAndReceivable ->
            onEvent(DebtAndReceivableScreenEvent.UpdateDebtAndReceivable(debtAndReceivable, context))
        },
        onDelete = { debtAndReceivableUuid ->
            onEvent(DebtAndReceivableScreenEvent.HardDeleteDebtAndReceivable(debtAndReceivableUuid, context))
        }
    )

    LazyColumn(Modifier.padding(top = 16.dp)) {
        item {
            Title("Daftar Hutang Piutang")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { query ->
                    onEvent(DebtAndReceivableScreenEvent.OnSearchQueryChange(query))
                },
                label = { Text("Cari hutang piutang ...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(uiState.debtAndReceivables) { debtAndReceivable ->
            ItemWithProgress(
                debtAndReceivable = debtAndReceivable,
                onClick = {
                    onEvent(DebtAndReceivableScreenEvent.SetSelectedDebtAndReceivable(debtAndReceivable))
                    onEvent(DebtAndReceivableScreenEvent.OpenDialog)
                }
            )
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = {
            onEvent(DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable)
            onEvent(DebtAndReceivableScreenEvent.OpenDialog)
        },
        contentDescription = "Tambah Hutang/Piutang"
    )
}

@PreviewLightDark
@Composable
fun DebtsAndReceivablesListScreenPreview() {
    SpeechnancialTheme {
        Surface {
            DebtsAndReceivablesListScreen(
                uiState = DebtAndReceivableScreenUiState(
                    debtAndReceivables = listOf(
                        DebtAndReceivable(
                            name = "Hutang ke Budi",
                            amount = 2000f,
                            paidAmount = 1000f,
                            dueDate = Timestamp.now(),
                            typeOrdinal = DebtType.DEBT.ordinal
                        ),
                        DebtAndReceivable(
                            name = "Piutang dari Ani",
                            amount = 2500f,
                            paidAmount = 1500f,
                            dueDate = Timestamp.now(),
                            typeOrdinal = DebtType.RECEIVABLE.ordinal
                        )
                    )
                ),
                onEvent = {}
            )
        }
    }
}