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
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogPairCode(
    showDialog: Boolean,
    initialPairCode: String = "",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    if (showDialog) {
        var pairCode by remember { mutableStateOf(TextFieldValue(initialPairCode)) }

        Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Pairing Smartwatch",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = pairCode,
                        onValueChange = { pairCode = it },
                        placeholder = { Text("masukkan pair key smartwatch") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Batal")
                        }
                        TextButton(onClick = {
                            onSave(pairCode.text)
                            onDismiss()
                        }) {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPairCodeDialog() {
    SpeechnancialTheme {
        Surface {
            DialogPairCode(
                showDialog = true,
                onDismiss = {},
                onSave = {}
            )
        }
    }
}
