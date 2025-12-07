package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogCategoryCrud(
    showDialog: Boolean,
    selectedCategory: Category = Category(enumTransactionType = EnumTransactionType.OUTCOME.ordinal),
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit,
    onUpdate: (Category) -> Unit,
    onDelete: ((String) -> Unit)
) {
    if (showDialog) {
        var categoryName by remember { mutableStateOf(TextFieldValue(selectedCategory.name)) }
        var transactionType by remember { mutableStateOf(
            EnumTransactionType.entries[selectedCategory.enumTransactionType]
        ) }
        var expandedTransactionType by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = onDismiss) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(if (selectedCategory.uuid.isEmpty()) "Tambah Kategori" else "Edit Kategori", style = MaterialTheme.typography.headlineSmall)

                    OutlinedTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Nama Kategori") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    CustomDropdown(
                        selectedItem = transactionType,
                        items = EnumTransactionType.entries.filter { it == EnumTransactionType.INCOME || it == EnumTransactionType.OUTCOME },
                        onItemSelected = { transactionType = it }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Batal")
                        }
                        if (selectedCategory.uuid.isNotEmpty()) {
                            TextButton(onClick = {
                                onDelete(selectedCategory.uuid)
                                onDismiss()
                            }) {
                                Text("Hapus")
                            }
                        }
                        TextButton(onClick = {
                            val category = Category(
                                uuid = selectedCategory.uuid,
                                name = categoryName.text,
                                enumTransactionType = transactionType.ordinal,
                                isDeleted = selectedCategory.isDeleted
                            )
                            if (selectedCategory.uuid == "") onSave(category)
                            else onUpdate(category)
                        }) {
                            Text(if (selectedCategory.uuid == "") "Simpan" else "Update")
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryDialogCreate() {
    SpeechnancialTheme {
        Surface {
            DialogCategoryCrud(
                showDialog = true,
                selectedCategory = Category(),
                onDismiss = {},
                onSave = {},
                onUpdate = {},
                onDelete = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryDialogUpdate() {
    SpeechnancialTheme {
        Surface {
            DialogCategoryCrud(
                showDialog = true,
                selectedCategory = Category(uuid = "123", name = "Makanan", enumTransactionType = EnumTransactionType.OUTCOME.ordinal),
                onDismiss = {},
                onSave = {},
                onUpdate = {},
                onDelete = {}
            )
        }
    }
}