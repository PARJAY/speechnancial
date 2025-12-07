package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun RelatedItemPickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    transactionType: TransactionType? = null,
    categories: List<Category>,
    savings: List<Saving>,
    debtAndReceivable: List<DebtAndReceivable>,
    wallets: List<Wallet>,
    onConfirm: ((relatedKindUUID: String, relatedKindName: String) -> Unit)? = null
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedSaving by remember { mutableStateOf<Saving?>(null) }
    var selectedDebtReceivable by remember { mutableStateOf<DebtAndReceivable?>(null) }
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }

    if (showDialog && transactionType != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    when (transactionType) {
                        TransactionType.EXPENSE, TransactionType.INCOME -> "Pilih Kategori"
                        TransactionType.SAVING_DEPOSIT, TransactionType.SAVING_WITHDRAWAL -> "Pilih Tabungan"
                        TransactionType.DEBT_PAYMENT -> "Pilih Hutang"
                        TransactionType.REGULAR_TRANSFER -> "Pilih Dompet Awal"
                        TransactionType.RECEIVABLE_PAYMENT -> "Pilih Piutang"
                        TransactionType.UNDEFINED -> "Pilih Jenis Transaksi Terlebih Dahulu"
                    }
                )
            },
            text = {
                Column {
                    when (transactionType) {
                        TransactionType.EXPENSE -> {
                            CustomDropdown(
                                selectedItem = selectedCategory,
                                items = categories.filter { it.enumTransactionType == EnumTransactionType.OUTCOME.ordinal },
                                onItemSelected = { category ->
                                    selectedCategory = category
                                },
                                itemToString = { it?.name ?: "Pilih Kategori" }
                            )
                        }
                        TransactionType.INCOME -> {
                            CustomDropdown(
                                selectedItem = selectedCategory,
                                items = categories.filter { it.enumTransactionType == EnumTransactionType.INCOME.ordinal },
                                onItemSelected = { category ->
                                    selectedCategory = category
                                },
                                itemToString = { it?.name ?: "Pilih Kategori" }
                            )
                        }
                        TransactionType.SAVING_DEPOSIT, TransactionType.SAVING_WITHDRAWAL -> {
                            CustomDropdown(
                                selectedItem = selectedSaving,
                                items = savings,
                                onItemSelected = { saving ->
                                    selectedSaving = saving
                                },
                                itemToString = { it?.name ?: "Pilih Tabungan" }
                            )
                        }
                        TransactionType.DEBT_PAYMENT -> {
                            CustomDropdown(
                                selectedItem = selectedDebtReceivable,
                                items = debtAndReceivable.filter { it.typeOrdinal != DebtType.RECEIVABLE.ordinal },
                                onItemSelected = { debtReceivable ->
                                    selectedDebtReceivable = debtReceivable
                                },
                                itemToString = { it?.name ?: "Pilih Hutang" }
                            )
                        }
                        TransactionType.RECEIVABLE_PAYMENT -> CustomDropdown(
                            selectedItem = selectedDebtReceivable,
                            items = debtAndReceivable.filter { it.typeOrdinal != DebtType.DEBT.ordinal },
                            onItemSelected = { debtReceivable ->
                                selectedDebtReceivable = debtReceivable
                            },
                            itemToString = { it?.name ?: "Pilih Piutang" }
                        )
                        TransactionType.REGULAR_TRANSFER -> {
                            CustomDropdown(
                                selectedItem = selectedWallet,
                                items = wallets,
                                onItemSelected = { wallet ->
                                    selectedWallet = wallet
                                },
                                itemToString = { it?.name ?: "Pilih Dompet Tujuan" }
                            )
                        }
                        TransactionType.UNDEFINED -> {}
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        when (transactionType) {
                            TransactionType.EXPENSE, TransactionType.INCOME -> selectedCategory?.let { onConfirm?.invoke(it.uuid, it.name) }
                            TransactionType.SAVING_DEPOSIT, TransactionType.SAVING_WITHDRAWAL -> selectedSaving?.let { onConfirm?.invoke(it.uuid, it.name) }
                            TransactionType.DEBT_PAYMENT -> selectedDebtReceivable?.let { onConfirm?.invoke(it.uuid, it.name) }
                            TransactionType.RECEIVABLE_PAYMENT -> selectedDebtReceivable?.let { onConfirm?.invoke(it.uuid, it.name) }
                            TransactionType.REGULAR_TRANSFER -> selectedWallet?.let { onConfirm?.invoke(it.uuid, it.name) }
                            TransactionType.UNDEFINED -> {}
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
fun RelatedItemPickerDialogPreviewExpense() {
    SpeechnancialTheme {
        Surface {
            RelatedItemPickerDialog(
                showDialog = true,
                onDismiss = {},
                transactionType = TransactionType.EXPENSE,
                categories = listOf(Category(name = "Makanan"), Category(name = "Transportasi")),
                savings = emptyList(),
                debtAndReceivable = emptyList(),
                wallets = emptyList(),
                onConfirm = { _, _ -> }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RelatedItemPickerDialogPreviewSaving() {
    SpeechnancialTheme {
        Surface {
            RelatedItemPickerDialog(
                showDialog = true,
                onDismiss = {},
                transactionType = TransactionType.SAVING_DEPOSIT,
                categories = emptyList(),
                savings = listOf(Saving(name = "Tabungan Rumah"), Saving(name = "Tabungan Pendidikan")),
                debtAndReceivable = emptyList(),
                wallets = emptyList(),
                onConfirm = { _, _ -> }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RelatedItemPickerDialogPreviewDebt() {
    SpeechnancialTheme {
        Surface {
            RelatedItemPickerDialog(
                showDialog = true,
                onDismiss = {},
                transactionType = TransactionType.DEBT_PAYMENT,
                categories = emptyList(),
                savings = emptyList(),
                debtAndReceivable = listOf(DebtAndReceivable(name = "Hutang A"), DebtAndReceivable(name = "Piutang B")),
                wallets = emptyList(),
                onConfirm = { _, _ -> }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun RelatedItemPickerDialogPreviewTransfer() {
    SpeechnancialTheme {
        Surface {
            RelatedItemPickerDialog(
                showDialog = true,
                onDismiss = {},
                transactionType = TransactionType.REGULAR_TRANSFER,
                categories = emptyList(),
                savings = emptyList(),
                debtAndReceivable = emptyList(),
                wallets = listOf(Wallet(name = "Dompet A"), Wallet(name = "Dompet B")),
                onConfirm = { _, _ -> }
            )
        }
    }
}