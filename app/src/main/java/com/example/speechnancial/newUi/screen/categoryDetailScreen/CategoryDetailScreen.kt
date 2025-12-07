package com.example.speechnancial.newUi.screen.categoryDetailScreen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
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
import com.example.speechnancial.newUi.component.dialogBox.DialogInputText
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactButton
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun CategoryDetailScreen(
    categoryName: String,
    keywords: List<String>,
    isNewCategory: Boolean,
    onCategoryNameClick: () -> Unit,
    onKeywordEditClick: (String) -> Unit,
    onKeywordDeleteClick: (String) -> Unit,
    onAddKeywordClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val scrollState = rememberScrollState() // State untuk scroll

    val showDropdownDialog = remember { mutableStateOf(false) }

    val selectedDataToEdit = remember { mutableStateOf("") }
    val initialText = remember { mutableStateOf("") }

    if (showDropdownDialog.value) {
        DialogInputText(
            onDismissRequest = {
                initialText.value = ""
                showDropdownDialog.value = !showDropdownDialog.value
            },
            onConfirmation = {
                // do something
                initialText.value = ""
                showDropdownDialog.value = !showDropdownDialog.value
            },
            inputLabel = "Keyword Kategori $",
            initialText = initialText.value
        )
    }

    // 1 modifikasi lagi column ini harus scrollable
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Tambahkan verticalScroll
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Edit,
                modifier = Modifier.clickable {
                    onCategoryNameClick()
                    initialText.value = categoryName
                    selectedDataToEdit.value = "Kategori"
                },
                contentDescription = "Edit Nama Kategori"
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tipe Transaksi : Pengeluaran",
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.Edit,
                modifier = Modifier.clickable {
                    // onCategoryTransactionTypeChanged()
                    // showDropdownDialogWithTransactionType()
                },
                contentDescription = "Edit Tipe Transaksi"
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        keywords.forEach { keyword ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = keyword,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    onKeywordEditClick(keyword)
                    initialText.value = keyword
                    selectedDataToEdit.value = "Keyword Kategori"
                    showDropdownDialog.value = true
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Kata Kunci")
                }
                IconButton(onClick = { onKeywordDeleteClick(keyword) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Edit Kata Kunci")
                }
            }
        }

        // agar item paling bawah tidak tertutupi box dibawah
        Spacer(modifier = Modifier.height(150.dp))
    }


    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = {
                onAddKeywordClick()

            },
            Modifier.padding(bottom = 56.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Tambah Keyword Kategori"
            )
        }
        Row (Modifier.fillMaxWidth()) {
            if(isNewCategory) {
                CompactButton(
                    onClick = { onSaveClick() },
                    text = "Simpan",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )

            }
            else {
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

// Placeholder functions for preview
@Composable
fun CategoryDetailScreenPreview() {
    CategoryDetailScreen(
        categoryName = "Makanan & Minuman",
        keywords = listOf("Makanan", "Minuman", "Restoran"),
        isNewCategory = false,
        onCategoryNameClick = {},
        onKeywordEditClick = {},
        onKeywordDeleteClick = {},
        onAddKeywordClick = {},
        onSaveClick = {},
        onUpdateClick = {},
        onDeleteClick = {}
    )
}

@PreviewLightDark
@Composable
fun PreviewCategoryDetailScreen() {
    SpeechnancialTheme {
        Surface {
            CategoryDetailScreenPreview()
        }
    }
}