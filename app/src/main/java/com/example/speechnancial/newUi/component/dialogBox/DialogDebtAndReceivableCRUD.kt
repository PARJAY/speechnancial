package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDebtAndReceivableCRUD(
    showDialog: Boolean,
    selectedDebtAndReceivable: DebtAndReceivable = DebtAndReceivable(),
    onDismiss: () -> Unit,
    onSave: (DebtAndReceivable) -> Unit,
    onUpdate: (DebtAndReceivable) -> Unit,
    onDelete: (String) -> Unit
) {
    if (showDialog) {
        var name by remember { mutableStateOf(TextFieldValue(selectedDebtAndReceivable.name)) }
        var amount by remember { mutableStateOf(TextFieldValue(if (selectedDebtAndReceivable.amount != 0.0f) selectedDebtAndReceivable.amount.toString() else "0")) }
        var paidAmount by remember { mutableStateOf(TextFieldValue(if (selectedDebtAndReceivable.paidAmount != 0.0f) selectedDebtAndReceivable.paidAmount.toString() else "0")) }
        var dueDate by remember { mutableStateOf(selectedDebtAndReceivable.dueDate) }
        var typeOrdinal by remember { mutableIntStateOf(selectedDebtAndReceivable.typeOrdinal) }

        val scope = rememberCoroutineScope()
        var showDatePicker by remember { mutableStateOf(false) }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dueDate.toDate().time
        )

        val context = LocalContext.current
        val localDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        var budgetDate by remember {
            mutableStateOf(
                Instant.ofEpochMilli(dueDate.toDate().time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        }

        Dialog(onDismissRequest = onDismiss) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        if (selectedDebtAndReceivable.uuid.isEmpty()) "Tambah Hutang/Piutang" else "Edit Hutang/Piutang",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = name.text.isEmpty()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            if (newValue.text.matches(Regex("[0-9]*"))) {
                                amount = newValue
                            }
                        },
                        label = { Text("Jumlah") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = amount.text.isEmpty()
                    )

                    Spacer(Modifier.height(8.dp))

                    if (selectedDebtAndReceivable.uuid.isNotEmpty())
                        OutlinedTextField(
                            value = paidAmount,
                            onValueChange = { newValue ->
                                if (newValue.text.matches(Regex("[0-9]*"))) {
                                    paidAmount = newValue
                                }
                            },
                            label = { Text("Jumlah Terbayar") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = paidAmount.text.isEmpty()
                        )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = budgetDate.format(localDateFormatter),
                        onValueChange = { },
                        label = { Text("Tanggal Jatuh Tempo") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                val now = budgetDate
                                val datePickerDialog = android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        budgetDate = LocalDate.of(year, month + 1, dayOfMonth)
                                        val instant = budgetDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                        dueDate = Timestamp(instant.epochSecond, instant.nano)
                                    },
                                    now.year, now.monthValue - 1, now.dayOfMonth
                                )
                                datePickerDialog.show()
                            }) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Pilih Tanggal")
                            }
                        }
                    )

                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    scope.launch {
                                        dueDate = Timestamp(datePickerState.selectedDateMillis!! / 1000, 0)
                                        showDatePicker = false
                                    }
                                }) {
                                    Text("Pilih")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Batal")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Mengganti OutlinedButton dengan RadioButton
                    if (selectedDebtAndReceivable.uuid.isEmpty())
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Tipe:")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = typeOrdinal == DebtType.DEBT.ordinal,
                                    onClick = { typeOrdinal = DebtType.DEBT.ordinal }
                                )
                                Text(text = "Hutang")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = typeOrdinal == DebtType.RECEIVABLE.ordinal,
                                    onClick = { typeOrdinal = DebtType.RECEIVABLE.ordinal }
                                )
                                Text(text = "Piutang")
                            }
                        }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Batal")
                        }
                        if (selectedDebtAndReceivable.uuid.isNotEmpty()) {
                            TextButton(onClick = {
                                onDelete(selectedDebtAndReceivable.uuid)
                                onDismiss()
                            }) {
                                Text("Hapus")
                            }
                        }
                        TextButton(onClick = {
                            if (name.text.isNotEmpty() && amount.text.isNotEmpty() && paidAmount.text.isNotEmpty()) {
                                val debtAndReceivable = DebtAndReceivable(
                                    uuid = selectedDebtAndReceivable.uuid,
                                    name = name.text,
                                    amount = amount.text.toFloat(),
                                    paidAmount = paidAmount.text.toFloat(),
                                    dueDate = dueDate,
                                    typeOrdinal = typeOrdinal
                                )
                                if (selectedDebtAndReceivable.uuid.isEmpty()) onSave(debtAndReceivable)
                                else onUpdate(debtAndReceivable)
                            }
                        }) {
                            Text(if (selectedDebtAndReceivable.uuid.isEmpty()) "Simpan" else "Update")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DialogDebtAndReceivableCRUDPreviewAdd() {
    SpeechnancialTheme {
        DialogDebtAndReceivableCRUD(
            showDialog = true,
            onDismiss = {},
            onSave = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogDebtAndReceivableCRUDPreviewEdit() {
    SpeechnancialTheme {
        DialogDebtAndReceivableCRUD(
            showDialog = true,
            selectedDebtAndReceivable = DebtAndReceivable(
                uuid = "123",
                name = "Hutang ke Budi",
                amount = 2000f,
                paidAmount = 1000f,
                dueDate = Timestamp.now(),
                typeOrdinal = DebtType.DEBT.ordinal
            ),
            onDismiss = {},
            onSave = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogDebtAndReceivableCRUDPreviewEmptyFields() {
    SpeechnancialTheme {
        DialogDebtAndReceivableCRUD(
            showDialog = true,
            selectedDebtAndReceivable = DebtAndReceivable(
                uuid = "123",
                name = "",
                amount = 0f,
                paidAmount = 0f,
                dueDate = Timestamp.now(),
                typeOrdinal = DebtType.DEBT.ordinal
            ),
            onDismiss = {},
            onSave = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}