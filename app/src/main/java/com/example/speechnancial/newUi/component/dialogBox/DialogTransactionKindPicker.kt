package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogTransactionKindPicker(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: ((TransactionType, TransactionTypeOld) -> Unit)? = null
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var selectedType by remember { mutableStateOf<TransactionTypeOld?>(TransactionTypeOld.UNDEFINED) }

    val transactionKindInIndonesiaList = remember {
        listOf(
            "Pengeluaran",
            "Pemasukan",
            "Transfer",
            "Setor Tabungan",
            "Tarik Tabungan",
            "Pembayaran Hutang",
            "Pembayaran Piutang",
        )
    }

    val validTransactionTypes = remember {
        TransactionType.entries.filter { it != TransactionType.UNDEFINED }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Pilih Jenis Transaksi") },
            text = {
                Column {
                    Text("Jenis Transaksi:")

                    CustomDropdown(
                        selectedItem = if (selectedIndex != -1) transactionKindInIndonesiaList.getOrNull(selectedIndex) else null,
                        items = transactionKindInIndonesiaList, // Menggunakan list Bahasa Indonesia
                        onItemSelected = { indonesianText ->
                            selectedIndex = transactionKindInIndonesiaList.indexOf(indonesianText)
                        },
                        itemToString = { it ?: "Pilih Jenis Transaksi" }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedIndex != -1 && selectedType != null) {
                            val selectedKind = validTransactionTypes.getOrNull(selectedIndex)
                            selectedKind?.let { onConfirm?.invoke(it, selectedType!!) }
                        }
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
fun TransactionKindPickerDialogPreview() {
    SpeechnancialTheme {
        Surface {
            DialogTransactionKindPicker(
                showDialog = true,
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }
    }
}