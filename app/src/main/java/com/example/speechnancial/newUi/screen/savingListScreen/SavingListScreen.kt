package com.example.speechnancial.newUi.screen.savingListScreen

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
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.viewmodel.SavingScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.SavingScreenUiState
import com.example.speechnancial.newUi.component.SavingItem
import com.example.speechnancial.newUi.component.dialogBox.DialogSavingCRUD
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun SavingListScreen(
    uiState: SavingScreenUiState,
    onEvent: (SavingScreenEvent) -> Unit
) {
    val context = LocalContext.current

    DialogSavingCRUD(
        showDialog = uiState.showDialogSavingCrud,
        selectedSaving = uiState.selectedSaving,
        onDismiss = { onEvent(SavingScreenEvent.CloseDialog) },
        onSave = { saving ->
            onEvent(SavingScreenEvent.SaveSaving(saving, context))
        },
        onUpdate = { saving ->
            onEvent(SavingScreenEvent.UpdateSaving(saving, context))
        },
        onDelete = { savingUuid ->
            onEvent(SavingScreenEvent.DeleteSaving(savingUuid, context))
        }
    )

    LazyColumn(Modifier.padding(top = 16.dp)) {
        item {
            Title("Daftar Tabungan")
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { query ->
                    onEvent(SavingScreenEvent.OnSearchQueryChange(query))
                },
                label = { Text("Cari tabungan...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(uiState.savings) { saving ->
            SavingItem(
                saving = saving,
                onClick = {
                    onEvent(SavingScreenEvent.SetSelectedSaving(saving))
                    onEvent(SavingScreenEvent.OpenDialog)
                }
            )
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = {
            onEvent(SavingScreenEvent.ResetSelectedSaving)
            onEvent(SavingScreenEvent.OpenDialog)
        },
        contentDescription = "Add Saving"
    )
}

@PreviewLightDark
@Composable
fun SavingListScreenPreview() {
    SpeechnancialTheme {
        Surface {
            SavingListScreen(
                uiState = SavingScreenUiState(
                    savings = listOf(
                        Saving(name = "Dana Pensiun", collectedAmount = 3000f, targetAmount = 5000f),
                        Saving(name = "Liburan", collectedAmount = 1000f, targetAmount = 2000f),
                        Saving(name = "lorem ipsum oolor sit amet Ajik ngutang buat beli paket internet", collectedAmount = 3000f, targetAmount = 5000f),
                    )
                ),
                onEvent = {}
            )
        }
    }
}