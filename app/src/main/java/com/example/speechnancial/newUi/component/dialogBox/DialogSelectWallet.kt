package com.example.speechnancial.newUi.component.dialogBox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.newUi.component.sharedComponent.CustomDropdown
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun DialogSelectWallet(
    onDismissRequest: () -> Unit,
    onConfirmation: (Wallet) -> Unit,
    title: String,
    wallets: List<Wallet>,
    selectedWallet: Wallet? = null
) {
    var selectedWalletState by remember { mutableStateOf(selectedWallet) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Column {
                Text("Pilih Dompet:")
                CustomDropdown(
                    selectedItem = selectedWalletState,
                    items = wallets,
                    onItemSelected = { wallet ->
                        selectedWalletState = wallet
                    },
                    itemToString = { it?.name ?: "Pilih Dompet" },
                )

                Spacer(modifier = Modifier.height(8.dp))

                selectedWalletState?.let {
                    Text("Saldo Tersisa: ${formatToThousandsSeparator(it.balance)}")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedWalletState?.let { onConfirmation(it) }
                }
            ) {
                Text("Konfirmasi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Batal")
            }
        }
    )
}


@PreviewLightDark
@Composable
fun DialogSelectWalletPreviewEmpty() {
    SpeechnancialTheme {
        Surface {
            DialogSelectWallet(
                onDismissRequest = {},
                onConfirmation = {},
                title = "Pilih Dompet",
                wallets = emptyList()
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DialogSelectWalletPreviewSingleSelected() {
    val wallets = listOf(
        Wallet(uuid = "1", name = "Dompet Utama", balance = 1000000f)
    )
    SpeechnancialTheme {
        Surface {
            DialogSelectWallet(
                onDismissRequest = {},
                onConfirmation = {},
                title = "Pilih Dompet",
                wallets = wallets,
                selectedWallet = wallets.first()
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DialogSelectWalletPreviewMultipleNoSelection() {
    val wallets = listOf(
        Wallet(uuid = "1", name = "Dompet Utama", balance = 1000000f),
        Wallet(uuid = "2", name = "Dompet Sekunder", balance = 500000f),
        Wallet(uuid = "3", name = "Dompet Investasi", balance = 2000000f)
    )
    SpeechnancialTheme {
        Surface {
            DialogSelectWallet(
                onDismissRequest = {},
                onConfirmation = {},
                title = "Pilih Dompet",
                wallets = wallets
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DialogSelectWalletPreviewMultipleWithSelection() {
    val wallets = listOf(
        Wallet(uuid = "1", name = "Dompet Utama", balance = 1000000f),
        Wallet(uuid = "2", name = "Dompet Sekunder", balance = 500000f),
        Wallet(uuid = "3", name = "Dompet Investasi", balance = 2000000f)
    )
    SpeechnancialTheme {
        Surface {
            DialogSelectWallet(
                onDismissRequest = {},
                onConfirmation = {},
                title = "Pilih Dompet",
                wallets = wallets,
                selectedWallet = wallets[1]
            )
        }
    }
}

@PreviewLightDark
@Composable
fun DialogSelectWalletPreviewZeroBalance() {
    val wallets = listOf(
        Wallet(uuid = "1", name = "Dompet Utama", balance = 1000000f),
        Wallet(uuid = "2", name = "Dompet Kosong", balance = 0f),
        Wallet(uuid = "3", name = "Dompet Lain", balance = 10000f)
    )
    SpeechnancialTheme {
        Surface {
            DialogSelectWallet(
                onDismissRequest = {},
                onConfirmation = {},
                title = "Pilih Dompet",
                wallets = wallets
            )
        }
    }
}