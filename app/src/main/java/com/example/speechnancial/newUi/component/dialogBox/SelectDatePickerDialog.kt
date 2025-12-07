package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTransactionDateDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (Timestamp) -> Unit,
) {
    if (showDialog) {
        val datePickerState = rememberDatePickerState()
        val scope = rememberCoroutineScope()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Convert millis to LocalDate and set time to start of day (00:00:00)
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            val instant = localDate
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()

                            val timestamp = Timestamp(instant.epochSecond, instant.nano)
                            onDateSelected(timestamp)
                            onDismiss()
                        }
                    }
                }) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@PreviewLightDark
@Composable
fun DatePickerDialogPreview() {
    SpeechnancialTheme {
        Surface {
            SelectTransactionDateDialog(
                showDialog = true,
                onDismiss = {},
                onDateSelected = {}
            )
        }
    }
}