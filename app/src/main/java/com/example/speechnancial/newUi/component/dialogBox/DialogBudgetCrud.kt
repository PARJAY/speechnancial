package com.example.speechnancial.newUi.component.dialogBox

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

// todo : on save is lacking
//  involved category uuid (dropdown)
//  transaction (income or spending) type ordinal

// selectedCategory should be multi select
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogBudgetCrud(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (Budget) -> Unit,
    onUpdate: (Budget) -> Unit,
    onDelete: (budgetUuid : String) -> Unit,
    categories: List<Category> = emptyList(),
    selectedBudget: Budget? = null
) {
    if (showDialog) {
        var budgetName by remember { mutableStateOf(TextFieldValue(selectedBudget?.name ?: "")) }
        var budgetAmount by remember { mutableStateOf(TextFieldValue(selectedBudget?.amount?.toString() ?: "")) }
        var selectedCategories by remember {
            mutableStateOf(
                selectedBudget?.involvedCategoriesUuid?.let { initialCategoryUuids ->
                    categories.filter { it.uuid in initialCategoryUuids }.toSet()
                } ?: emptySet()
            )
        }
        var transactionTypeOrdinal by remember { mutableIntStateOf(selectedBudget?.transactionTypeOrdinal ?: EnumTransactionType.OUTCOME.ordinal) }
        var timeRange by remember { mutableStateOf(EnumTimeRange.entries[selectedBudget?.recurringTypeOrdinal ?: EnumTimeRange.NOT_RECURRING.ordinal]) }
        var customDays by remember { mutableStateOf(TextFieldValue(selectedBudget?.timeRangeInDays?.toString() ?: "")) }
        var budgetDate by remember { mutableStateOf(selectedBudget?.startTime?.toDate()?.toInstant()?.atOffset(ZoneOffset.UTC)?.toLocalDate() ?: LocalDate.now()) }
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = java.util.Date()

        Dialog(onDismissRequest = onDismiss) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(if (selectedBudget == null) "Tambah Budget" else "Edit Budget", style = MaterialTheme.typography.headlineSmall)

                    OutlinedTextField(
                        value = budgetName,
                        onValueChange = { budgetName = it },
                        label = { Text("Nama Budget") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = budgetAmount,
                        onValueChange = { budgetAmount = it },
                        label = { Text("Jumlah Anggaran") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    CustomDropdown(
                        selectedItem = EnumTransactionType.entries[transactionTypeOrdinal],
                        items = EnumTransactionType.entries.toList().filter { it.ordinal == EnumTransactionType.OUTCOME.ordinal || it.ordinal == EnumTransactionType.INCOME.ordinal},
                        onItemSelected = { transactionTypeOrdinal = it.ordinal },
                        itemToString = { it.toString() }
                    )

                    Spacer(Modifier.height(8.dp))

                    // Multi-select dropdown for categories
                    CustomDropdown(
                        selectedItem = null, // Start with no selection in dropdown
                        items = categories.filter { it.enumTransactionType == transactionTypeOrdinal }
                            .filter { it !in selectedCategories }, //show only not selected
                        onItemSelected = { category ->
                            if (category != null) {
                                selectedCategories = selectedCategories + category
                            }
                        },
                        itemToString = { it?.name ?: "Pilih Kategori" }
                    )

                    Spacer(Modifier.height(8.dp))

                    // Chip group to display selected categories
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        selectedCategories.forEach { category ->
                            InputChip(
                                selected = true,
                                onClick = {},
                                label = { Text(category.name) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Hapus Kategori",
                                        modifier = Modifier.clickable {
                                            selectedCategories = selectedCategories - category
                                        }
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    CustomDropdown(
                        selectedItem = timeRange,
                        items = EnumTimeRange.entries.toList(),
                        onItemSelected = { timeRange = it }
                    )

                    Spacer(Modifier.height(8.dp))

                    if (timeRange == EnumTimeRange.CUSTOM) {
                        OutlinedTextField(
                            value = customDays,
                            onValueChange = { customDays = it },
                            label = { Text("Jumlah Hari") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = budgetDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        onValueChange = { },
                        label = { Text("Tanggal Pelaksanaan") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        budgetDate = LocalDate.of(year, month + 1, dayOfMonth)
                                    }, year, month, day
                                )
                                datePickerDialog.show()
                            }) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Pilih Tanggal")
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Batal")
                        }

                        if (selectedBudget != null) {
                            TextButton(onClick = { onDelete(selectedBudget.uuid) }) {
                                Text("Hapus")
                            }
                        }

                        TextButton(onClick = {
                            val days = if (timeRange == EnumTimeRange.CUSTOM) customDays.text.toIntOrNull() else null
                            val budget = Budget(
                                uuid = selectedBudget?.uuid ?: "", // Menggunakan uuid yang ada jika edit
                                name = budgetName.text,
                                amount = budgetAmount.text.toFloatOrNull() ?: 0f,
                                startTime = Timestamp(budgetDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC), 0),
                                timeRangeInDays = days ?: 0,
//                                var timeRange by remember { mutableStateOf(EnumTimeRange.entries[selectedBudget?.recurringTypeOrdinal ?: EnumTimeRange.NOT_RECURRING.ordinal]) }
                                recurringTypeOrdinal = timeRange.ordinal,
                                involvedCategoriesUuid = selectedCategories.map { it.uuid }.toList(), // Convert Set to List
                                transactionTypeOrdinal = transactionTypeOrdinal,
                                budgetRealizations = listOf(),
                                isDeleted = selectedBudget?.isDeleted ?: false
                            )
                            if (selectedBudget == null) onSave(budget) else onUpdate(budget)
                        }) {
                            Text(if (selectedBudget == null) "Simpan" else "Update")
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewBudgetDialogAdd() {
    DialogBudgetCrud(
        showDialog = true,
        onDismiss = {},
        onSave = { _ -> },
        onUpdate = { _ -> },
        onDelete = { _ -> },
        categories = listOf(Category(name = "whatever"))
    )
}

@PreviewLightDark
@Composable
fun PreviewBudgetDialogEdit() {
    val budget = Budget(
        uuid = "123",
        name = "Budget Makanan",
        amount = 1000f,
        startTime = Timestamp.now(),
        timeRangeInDays = 30,
        recurringTypeOrdinal = EnumTimeRange.MONTHLY.ordinal,
        involvedCategoriesUuid = listOf("cat1"),
        transactionTypeOrdinal = EnumTransactionType.OUTCOME.ordinal
    )

    DialogBudgetCrud(
        showDialog = true,
        onDismiss = {},
        onSave = { _ -> },
        onUpdate = { _ -> },
        onDelete = { _ -> },
        categories = listOf(Category(uuid = "cat1", name = "Makanan")),
        selectedBudget = budget
    )
}