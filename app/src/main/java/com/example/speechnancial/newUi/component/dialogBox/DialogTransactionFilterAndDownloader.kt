package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.viewmodel.TransactionScreenUiState
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogTransactionFilterAndDownloader(
    uiState: TransactionScreenUiState,
    onWalletSelected: (List<Wallet>) -> Unit,
    onTimeRangeSelected: (EnumTimeRange) -> Unit,
    onCustomDateRangeSelected: (Pair<Timestamp, Timestamp>) -> Unit,
    onSaveSettings: (String) -> Unit,
    onDownloadAllTransactions: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedWallets by remember { mutableStateOf(uiState.filteredWallet) }
    var selectedTimeRange by remember { mutableStateOf(uiState.timeRange) }
    val customStartDate by remember { mutableStateOf(uiState.customDateRange?.first) }
    val customEndDate by remember { mutableStateOf(uiState.customDateRange?.second) }

    var pairCode by remember { mutableStateOf(TextFieldValue("")) } // ⬅ state untuk Pair Code

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter & Download Transaksi") },
        text = {
            Column {
                // 🔑 Input Pair Code
                OutlinedTextField(
                    value = pairCode,
                    onValueChange = { pairCode = it },
                    placeholder = { Text("masukkan pair key smartwatch") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("Dompet:")
                CustomDropdown(
                    selectedItem = null,
                    items = uiState.wallets.filter { !selectedWallets.contains(it) },
                    onItemSelected = { wallet ->
                        if (wallet != null) selectedWallets = selectedWallets + wallet
                    },
                    itemToString = { it?.name ?: "Pilih Dompet Disini" }
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    selectedWallets.forEach { wallet ->
                        InputChip(
                            selected = true,
                            onClick = {},
                            label = { Text(wallet.name) },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Hapus Dompet",
                                    modifier = Modifier.clickable {
                                        selectedWallets = selectedWallets - wallet
                                    }
                                )
                            }
                        )
                    }
                }

                Text("Rentang Waktu:")
                CustomDropdown(
                    selectedItem = selectedTimeRange,
                    items = EnumTimeRange.entries,
                    onItemSelected = { timeRange ->
                        selectedTimeRange = timeRange
                    },
                    itemToString = { it.toString() }
                )

                if (selectedTimeRange == EnumTimeRange.CUSTOM) {
                    Text("Tanggal Kustom:")
                    Row {
                        Text("Mulai:")
                        Text(customStartDate?.seconds?.toString() ?: "Pilih Tanggal")
                        Spacer(Modifier.width(8.dp))
                        Text("Akhir:")
                        Text(customEndDate?.seconds?.toString() ?: "Pilih Tanggal")
                    }
                }
            }
        },
        confirmButton = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Button(onClick = {
                        onWalletSelected(selectedWallets)
                        onTimeRangeSelected(selectedTimeRange!!)
                        if (selectedTimeRange == EnumTimeRange.CUSTOM && customStartDate != null && customEndDate != null) {
                            onCustomDateRangeSelected(Pair(customStartDate!!, customEndDate!!))
                        }
                        onSaveSettings(pairCode.text)
                    }) {
                        Text("Simpan Pengaturan")
                    }
                }
                Button(onClick = onDownloadAllTransactions) {
                    Text("Unduh Semua Transaksi")
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
fun DialogTransactionFilterAndDownloaderPreview() {
    SpeechnancialTheme {
        Surface {
            DialogTransactionFilterAndDownloader(
                uiState = TransactionScreenUiState(
                    wallets = listOf(
                        Wallet(name = "Dompet 1"),
                        Wallet(name = "Dompet 2"),
                        Wallet(name = "Dompet 3")
                    ),
                    filteredWallet = listOf(Wallet(name = "Dompet 1")),
                    timeRange = EnumTimeRange.DAILY,
                    customDateRange = Pair(Timestamp.now(), Timestamp.now())
                ),
                onWalletSelected = {},
                onTimeRangeSelected = {},
                onCustomDateRangeSelected = {},
                onSaveSettings = { _ -> },
                onDownloadAllTransactions = {},
                onDismiss = {}
            )
        }
    }
}