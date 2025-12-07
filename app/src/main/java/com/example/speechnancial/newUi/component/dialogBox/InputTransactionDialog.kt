package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet

// dengan parameter sebanyak ini, bukankah lebih baik jika memasukkan InputTransactionState saja?
@Composable
fun InputTransactionDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    transactionType: TransactionType?,
    onTransactionKindSelected: (TransactionType) -> Unit,
    relatedItem: Pair<String, String>?,
    onRelatedItemSelected: (String, String) -> Unit,
    wallets: List<Wallet>,
    categories: List<Category>,
    savings: List<Saving>,
    debtAndReceivables: List<DebtAndReceivable>,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Pilih Jenis Transaksi") },
            text = {
                LazyColumn {
                    item {
                        Text("Jenis Transaksi:")
                        TransactionType.entries.forEach { kind ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTransactionKindSelected(kind) }
                                    .padding(8.dp)
                            ) {
                                Text(kind.toString())
                            }
                        }
                    }

                    if (transactionType != null) {
                        item {
                            Text("Pilihan Kedua:")
                            when (transactionType) {
                                TransactionType.EXPENSE, TransactionType.INCOME -> {
                                    categories.forEach { category ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onRelatedItemSelected(category.uuid, "category") }
                                                .padding(8.dp)
                                        ) {
                                            Text(category.name)
                                        }
                                    }
                                }
                                TransactionType.SAVING_DEPOSIT, TransactionType.SAVING_WITHDRAWAL -> {
                                    savings.forEach { saving ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onRelatedItemSelected(saving.uuid, "saving") }
                                                .padding(8.dp)
                                        ) {
                                            Text(saving.name)
                                        }
                                    }
                                }
                                TransactionType.DEBT_PAYMENT -> {
                                    debtAndReceivables.forEach { debtReceivable ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onRelatedItemSelected(debtReceivable.uuid, "debtReceivable") }
                                                .padding(8.dp)
                                        ) {
                                            Text(debtReceivable.name)
                                        }
                                    }
                                }

                                TransactionType.UNDEFINED -> TODO() // kunci pilihan kedua jika masih undefined
                                TransactionType.REGULAR_TRANSFER -> TODO()  // dompet
                                TransactionType.RECEIVABLE_PAYMENT -> TODO()
                            }
                        }

                        item {
                            Text("Pilihan Ketiga:")
                            wallets.forEach { wallet ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onRelatedItemSelected(wallet.uuid, "wallet") }
                                        .padding(8.dp)
                                ) {
                                    Text(wallet.name)
                                }
                            }
                        }

                        // Tambahkan pilihan keempat (tanggal) dan pilihan keenam (checkbox edit nanti) di sini
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}