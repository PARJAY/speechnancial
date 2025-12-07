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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSavingCRUD(
    showDialog: Boolean,
    selectedSaving: Saving = Saving(),
    onDismiss: () -> Unit,
    onSave: (Saving) -> Unit,
    onUpdate: (Saving) -> Unit,
    onDelete: (String) -> Unit
) {
    if (showDialog) {
        var savingName by remember { mutableStateOf(TextFieldValue(selectedSaving.name.ifEmpty { "" })) }
        var collectedAmount by remember { mutableStateOf(TextFieldValue(if (selectedSaving.collectedAmount != 0.0f) selectedSaving.collectedAmount.toString() else "0")) }
        var targetAmount by remember { mutableStateOf(TextFieldValue(if (selectedSaving.targetAmount != 0.0f) selectedSaving.targetAmount.toString() else "0")) }
        var savingTargetDate by remember { mutableStateOf(selectedSaving.savingTargetDate) }

        val scope = rememberCoroutineScope()
        var showDatePicker by remember { mutableStateOf(false) }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = savingTargetDate.toDate().time
        )

        val context = LocalContext.current
        val localDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        var targetDate by remember {
            mutableStateOf(
                Instant.ofEpochMilli(savingTargetDate.toDate().time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        }

        Dialog(onDismissRequest = onDismiss) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        if (selectedSaving.uuid.isEmpty()) "Tambah Tabungan" else "Edit Tabungan",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = savingName,
                        onValueChange = { savingName = it },
                        label = { Text("Nama Tabungan") },
                        modifier = Modifier.fillMaxWidth(),
//                        isError = savingName.text.isEmpty()
                    )

                    Spacer(Modifier.height(8.dp))

                    if (selectedSaving.uuid.isNotEmpty())
                        OutlinedTextField(
                            value = collectedAmount,
                            onValueChange = { newValue ->
                                if (newValue.text.matches(Regex("[0-9]*"))) {
                                    collectedAmount = newValue
                                }
                            },
                            readOnly = true,
                            label = { Text("Saldo Tabungan Saat Ini") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = collectedAmount.text.isEmpty()
                        )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = targetAmount,
                        onValueChange = { newValue ->
                            targetAmount = newValue
                        },
                        label = { Text("Target Akhir Tabungan") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = targetAmount.text.isEmpty()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = targetDate.format(localDateFormatter),
                        onValueChange = { },
                        label = { Text("Target Tanggal") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                val now = targetDate
                                val datePickerDialog = android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        targetDate = LocalDate.of(year, month + 1, dayOfMonth)
                                        val instant = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                        savingTargetDate = Timestamp(instant.epochSecond, instant.nano)
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
                                        savingTargetDate = Timestamp(datePickerState.selectedDateMillis!! / 1000, 0)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Batal")
                        }
                        if (selectedSaving.uuid.isNotEmpty()) {
                            TextButton(onClick = {
                                onDelete(selectedSaving.uuid)
                                onDismiss()
                            }) {
                                Text("Hapus")
                            }
                        }
                        TextButton(onClick = {
                            if (savingName.text.isNotEmpty() && collectedAmount.text.isNotEmpty() && targetAmount.text.isNotEmpty()) {
                                val saving = Saving(
                                    uuid = selectedSaving.uuid,
                                    name = savingName.text,
                                    collectedAmount = collectedAmount.text.toFloat(),
                                    targetAmount = targetAmount.text.toFloat(),
                                    savingTargetDate = savingTargetDate
                                )
                                if (selectedSaving.uuid.isEmpty()) onSave(saving)
                                else onUpdate(saving)
                            }
                        }) {
                            Text(if (selectedSaving.uuid.isEmpty()) "Simpan" else "Update")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSavingDialogCreate() {
    SpeechnancialTheme {
        Surface {
            DialogSavingCRUD(
                showDialog = true,
                selectedSaving = Saving(),
                onDismiss = {},
                onSave = {},
                onUpdate = {},
                onDelete = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSavingDialogUpdate() {
    SpeechnancialTheme {
        Surface {
            DialogSavingCRUD(
                showDialog = true,
                selectedSaving = Saving(
                    uuid = "456",
                    name = "Dana Pensiun",
                    collectedAmount = 1000000.0f,
                    targetAmount = 5000000.0f,
                    savingTargetDate = Timestamp.now()
                ),
                onDismiss = {},
                onSave = {},
                onUpdate = {},
                onDelete = {}
            )
        }
    }
}