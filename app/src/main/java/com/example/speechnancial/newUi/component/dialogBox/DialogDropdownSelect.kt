package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDropdownSelect(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    title: String,
    options: List<String>,
    selectedOption: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption ?: options.firstOrNull() ?: "") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = title)
        },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedText,
                        onValueChange = { },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedText = selectionOption
                                    expanded = false
                                },
                                text = {
                                    Text(text = selectionOption)
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(selectedText)
                }
            ) {
                Text("Konfirmasi")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Batal")
            }
        }
    )
}

data class DialogDropdownSelectPreviewParameter(
    val title: String,
    val options: List<String>
)

class DialogDropdownSelectPreviewProvider :
    PreviewParameterProvider<DialogDropdownSelectPreviewParameter> {
    override val values: Sequence<DialogDropdownSelectPreviewParameter> = sequenceOf(
        DialogDropdownSelectPreviewParameter(
            title = "Tambahkan Kategori",
            options = listOf("Kategori 1", "Kategori 2", "Kategori 3")
        ),
        DialogDropdownSelectPreviewParameter(
            title = "Pilih Kategori",
            options = listOf("Kategori 1", "Kategori 2", "Kategori 3")
        ),
        DialogDropdownSelectPreviewParameter(
            title = "Pilih Budget",
            options = listOf("Budget 1", "Budget 2", "Budget 3")
        ),
        DialogDropdownSelectPreviewParameter(
            title = "Pilih Dompet",
            options = listOf("Dompet 1", "Dompet 2", "Dompet 3")
        )
    )
}

@PreviewLightDark
@Composable
fun PreviewDialogDropdownSelect(
    @PreviewParameter(DialogDropdownSelectPreviewProvider::class) parameter: DialogDropdownSelectPreviewParameter
) {
    SpeechnancialTheme {
        DialogDropdownSelect(
            onDismissRequest = {},
            onConfirmation = {},
            title = parameter.title,
            options = parameter.options,
            selectedOption = null
        )
    }
}