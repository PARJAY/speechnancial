package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogInputText(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    inputLabel: String, // Nama variabel yang lebih sesuai untuk label input
    initialText: String = "" // Nama variabel yang lebih sesuai untuk teks awal
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = if (initialText.isEmpty()) "Masukkan Nama $inputLabel" else "Edit $inputLabel"
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(text = "Nama $inputLabel")
                    },
                    isError = text.isEmpty(),
                    supportingText = {
                        Text(if (text.isEmpty()) "*wajib diisi" else "")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation(text) }
            ) {
                Text("Konfirmasi")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text("Batal")
            }
        }
    )
}

data class DialogInputTextPreviewParameter(
    val inputLabel: String,
    val initialText: String
)

class DialogInputTextPreviewProvider : PreviewParameterProvider<DialogInputTextPreviewParameter> {
    override val values: Sequence<DialogInputTextPreviewParameter> = sequenceOf(
        DialogInputTextPreviewParameter(
            inputLabel = "Dompet",
            initialText = ""
        ),
        DialogInputTextPreviewParameter(
            inputLabel = "Keyword Dompet",
            initialText = ""
        ),
        DialogInputTextPreviewParameter(
            inputLabel = "Kategori",
            initialText = "Makanan"
        ),
        DialogInputTextPreviewParameter(
            inputLabel = "Keyword Kategori Makanan",
            initialText = "Oreo"
        ),
        DialogInputTextPreviewParameter(
            inputLabel = "Budget",
            initialText = "Budget Bulanan"
        )
    )
}

@PreviewLightDark
@Composable
fun PreviewDialogInputText(
    @PreviewParameter(DialogInputTextPreviewProvider::class) parameter: DialogInputTextPreviewParameter
) {
    SpeechnancialTheme {
        DialogInputText(
            onDismissRequest = {},
            onConfirmation = {},
            inputLabel = parameter.inputLabel,
            initialText = parameter.initialText
        )
    }
}