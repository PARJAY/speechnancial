package com.example.speechnancial.ui.component.transactionListScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogEditTransaction(
    selectedTransaction: Transaction,
    onUserInput: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirmUpdate: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Edit Transaksi") },

        text = {
            Column {
                OutlinedTextField(
                    value = selectedTransaction.rawText,
                    onValueChange = {onUserInput(it)},
                    label = {
                        Text("Silahkan Edit Transaksi")
                    },
                    isError = selectedTransaction.rawText.isEmpty(),
                    supportingText = {
                        Text(
                            if (selectedTransaction.rawText.isEmpty()) "*required"
                            else ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },

        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Batal")
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedTransaction.rawText.isEmpty()) return@TextButton
                    onConfirmUpdate()
                }
            ) {
                Text("Confirm")
            }
        },
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MultipleChoiceDialogForProductSellingKitChoicePreview() {
    SpeechnancialTheme {
        Surface {
            DialogEditTransaction(
                Transaction(),
                onDismiss = {},
                onUserInput = {},
                onConfirmUpdate = {

                },
            )
        }
    }
}