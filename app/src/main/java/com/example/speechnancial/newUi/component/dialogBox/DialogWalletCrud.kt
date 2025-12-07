package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogWalletCrud(
    wallet: Wallet = Wallet(),
    onDismiss: () -> Unit,
    onSave: (Wallet) -> Unit,
    onUpdate: (Wallet) -> Unit,
    onDelete: (String) -> Unit
) {
    var walletName by remember { mutableStateOf(TextFieldValue(wallet.name)) }
    var walletBalance by remember { mutableStateOf(TextFieldValue(wallet.balance.toString())) }
    var isDefault by remember { mutableStateOf(wallet.isDefaultWallet) }

    var nameError by remember { mutableStateOf(false) }
    var balanceError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (wallet.uuid.isNotEmpty()) "Edit Dompet" else "Tambah Dompet",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = walletName,
                    onValueChange = { walletName = it; nameError = false },
                    label = { Text("Nama Dompet") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    supportingText = { if(nameError) Text("Nama tidak boleh kosong") }
                )

//                Spacer(modifier = Modifier.height(8.dp))

                // todo : angka 0 tidak boleh ada didepan jika tidak ada titik dan koma
                OutlinedTextField(
                    value = walletBalance,
                    onValueChange = {
                        walletBalance = it
                        balanceError = false
                    },
//                    visualTransformation = ThousandSeparatorTransformation(),
                    label = { Text("Saldo") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = balanceError,
                    supportingText = { if (balanceError) Text("Saldo harus berupa angka") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Hanya menerima angka
                )

                if (wallet.uuid.isNotEmpty()) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Checkbox(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it },
                            enabled = !isDefault
                        )
                        Text("Jadikan Dompet Utama")
                    }
                    Text(
                        "*Dompet Utama saat ini akan menjadi dompet biasa",
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }

                    if (wallet.uuid.isNotEmpty()) {
                        TextButton(
                            onClick = { onDelete(wallet.uuid) },
                            enabled = !isDefault
                        ) {
                            Text("Hapus")
                        }
                    }

                    TextButton(onClick = {
                        if (walletName.text.isEmpty()) {
                            nameError = true
                        }
                        if (walletBalance.text.toFloatOrNull() == null) {
                            balanceError = true
                        }

                        if (!nameError && !balanceError) {
                            val walletData = wallet.copy(
                                name = walletName.text,
                                balance = walletBalance.text.toFloatOrNull() ?: 0.0f,
                                isDefaultWallet = isDefault
                            )
                            if (wallet.uuid.isNotEmpty()) onUpdate(walletData) else onSave(walletData)
                        }
                    }) {
                        Text(if (wallet.uuid.isNotEmpty()) "Update" else "Simpan")
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AddWalletDialogPreview() {
    SpeechnancialTheme {
        DialogWalletCrud(
            wallet = Wallet(), // Wallet kosong untuk tambah
            onDismiss = {},
            onSave = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}

@PreviewLightDark
@Composable
fun EditWalletDialogPreview() {
    SpeechnancialTheme {
        DialogWalletCrud(
            wallet = Wallet(uuid = "dompet lama ni", name = "Dompet Fisik", balance = 1000.0f, isDefaultWallet = true),
            onDismiss = {},
            onSave = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}