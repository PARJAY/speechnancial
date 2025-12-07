package com.example.speechnancial.newUi.screen.budgetDetailScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.newUi.component.dialogBox.DialogDropdownSelect
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactButton
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun BudgetDetailScreenOld(
    budgetName: String,
    budgetAllocation: String,
    categories: List<String>,
    isNewBudget: Boolean,
    onBudgetNameClick: () -> Unit,
    onBudgetAllocationClick: () -> Unit,
    onCategoryDeleteClick: (String) -> Unit,
    onAddCategoryClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val showDropdownDialog = remember { mutableStateOf(false) }
    val options = listOf("Kategori 1", "Kategori 2", "Kategori 3")

    if (showDropdownDialog.value) {
        DialogDropdownSelect(
            onDismissRequest = { showDropdownDialog.value = false },
            onConfirmation = { selectedCategory ->
                // Lakukan sesuatu dengan kategori yang dipilih
                println("Kategori yang dipilih: $selectedCategory")
                showDropdownDialog.value = false
            },
            title = "Pilih Kategori",
            options = options
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = budgetName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f).clickable(onClick = onBudgetNameClick)
            )
            IconButton(onClick = onBudgetNameClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Nama Budget")
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Alokasi: $budgetAllocation", modifier = Modifier.weight(1f))
            IconButton(onClick = onBudgetAllocationClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Alokasi Budget")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        categories.forEach { category ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onCategoryDeleteClick(category) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus Kategori")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CompactButton(
            onClick = {
                onAddCategoryClick()
                showDropdownDialog.value = !showDropdownDialog.value
            },
            text = "Tambah Kategori",
            Modifier.fillMaxWidth(),
            MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(150.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Row(Modifier.fillMaxWidth()) {
            if (isNewBudget) {
                CompactButton(
                    onClick = { onSaveClick() },
                    text = "Simpan",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            } else {
                CompactButton(
                    onClick = { onDeleteClick() },
                    text = "Hapus",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(Modifier.padding(4.dp))

                CompactButton(
                    onClick = { onUpdateClick() },
                    text = "Perbarui",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun BudgetScreenPreview() {
    SpeechnancialTheme {
        Surface {
            BudgetDetailScreenOld(
                budgetName = "Budget Makan Bulanan",
                budgetAllocation = "Rp. 2.000.000",
                categories = listOf("Makanan Pokok", "Cemilan", "Minuman"),
                isNewBudget = false,
                onBudgetNameClick = {},
                onBudgetAllocationClick = {},
                onCategoryDeleteClick = {},
                onAddCategoryClick = {},
                onSaveClick = {},
                onUpdateClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBudgetScreen() {
    BudgetScreenPreview()
}