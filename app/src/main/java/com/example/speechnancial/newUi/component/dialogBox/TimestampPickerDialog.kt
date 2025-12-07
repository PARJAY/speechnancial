package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimestampPickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Timestamp) -> Unit,
    initialDate: Timestamp? = null
) {
    val initialLocalDate = initialDate?.let {
        Instant.ofEpochSecond(it.seconds, it.nanoseconds.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().truncatedTo(ChronoUnit.DAYS)
                        val timestamp = Timestamp(instant.epochSecond, 0)
                        onDateSelected(timestamp)
                        onDismissRequest()
                    }
                }
            ) {
                Text("Konfirmasi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Batal")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}


@PreviewLightDark
@Composable
fun TimestampPickerDialogPreview() {
    Surface {
        SpeechnancialTheme {
            var showDialog by remember { mutableStateOf(true) } // Preview show dialog by default
            var selectedTimestamp by remember { mutableStateOf<Timestamp?>(null) }

            if (showDialog) {
                TimestampPickerDialog(
                    onDismissRequest = { showDialog = false },
                    onDateSelected = { timestamp ->
                        selectedTimestamp = timestamp
                        println("Selected Timestamp: $timestamp")
                    },
                    initialDate = null
                )
            }
        }
    }
}