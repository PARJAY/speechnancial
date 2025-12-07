package com.example.speechnancial.newUi.screen.walletListScreen

import WalletItem
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.viewmodel.WalletScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.WalletScreenUiState
import com.example.speechnancial.newUi.component.dialogBox.DialogWalletCrud
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun WalletListScreen(
    uiState: WalletScreenUiState,
    onEvent: (WalletScreenEvent) -> Unit,

) {
    val context = LocalContext.current

    if (uiState.showDialogWalletCrud) {
        DialogWalletCrud(
            wallet = uiState.selectedWallet,
            onDismiss = {
                onEvent(WalletScreenEvent.CloseDialog)
                onEvent(WalletScreenEvent.ClearSelectedWallet)
            },
            onSave = { wallet ->
                onEvent(WalletScreenEvent.SaveWallet(wallet) { message ->
                     Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
            },
            onUpdate = { wallet ->
                onEvent(WalletScreenEvent.UpdateWallet(wallet) { message ->
                     Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
                onEvent(WalletScreenEvent.ClearSelectedWallet)
            },
            onDelete = { walletUuid ->
                onEvent(WalletScreenEvent.DeleteWallet(walletUuid) { message ->
                     Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
                onEvent(WalletScreenEvent.ClearSelectedWallet)
            }
        )
    }

    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Title("Daftar Dompet")
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { query ->
                    onEvent(WalletScreenEvent.OnSearchQueryChange(query))
                },
                label = { Text("Cari dompet...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(uiState.wallets) { wallet ->
            Spacer(modifier = Modifier.height(8.dp))
            WalletItem(
                wallet = wallet,
                onClick = { onEvent(WalletScreenEvent.SetSelectedWallet(wallet)) }
            )
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = { onEvent(WalletScreenEvent.OpenDialog) },
        contentDescription = "Add Wallet"
    )
}


// i want to add bottom navigation bar to the preview that selecting wallet menu
// Add Bottom navigation to the preview
@PreviewLightDark
@Composable
fun WalletListScreenPreview() {
    SpeechnancialTheme {
        Surface {
            Column {
                WalletListScreen(
                    uiState = WalletScreenUiState(
                        wallets = listOf(
                            Wallet(name = "Dompet Utama", balance = 1000.0f, totalEarning = 500.0f, totalSpending = 200.0f),
                            Wallet(name = "Dompet Tabungan", balance = 5000.0f, totalEarning = 2000.0f, totalSpending = 100.0f)
                        )
                    ),
                    onEvent = {}
                )
                // Bottom navigation placeholder
                Text("Bottom Navigation Placeholder", modifier = Modifier.padding(16.dp))
            }
        }
    }
}