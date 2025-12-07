package com.example.speechnancial.newUi.screen.walletDetailScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactButton
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun WalletDetailScreen(
    walletName: String,
    balance: String, // Tambahkan parameter balance
    keywords: List<String>,
    isNewWallet: Boolean,
    onWalletNameClick: () -> Unit,
    onKeywordEditClick: (String) -> Unit,
    onKeywordDeleteClick: (String) -> Unit,
    onAddKeywordClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData: () -> Unit // Tambahkan parameter opname
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = walletName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.clickable(onClick = onWalletNameClick)
                )
                IconButton(onClick = onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Nama Wallet")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Balance: $balance")
                IconButton(onClick = onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData) {
                    Icon(Icons.Default.Edit, contentDescription = "Opname Balance")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // opname

//        keywords.forEach { keyword ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = keyword,
//                    modifier = Modifier.weight(1f)
//                )
//                IconButton(onClick = { onKeywordEditClick(keyword) }) {
//                    Icon(Icons.Default.Edit, contentDescription = "Edit Kata Kunci")
//                }
//                IconButton(onClick = { onKeywordDeleteClick(keyword) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Hapus Kata Kunci")
//                }
//            }
//        }

        Spacer(modifier = Modifier.height(150.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            onClick = { onAddKeywordClick() },
            Modifier.padding(bottom = 56.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.tertiary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Tambah Keyword Dompet"
            )
        }
        Row(Modifier.fillMaxWidth()) {
            if (isNewWallet) {
                CompactButton(
                    onClick = { onSaveClick() },
                    text = "Simpan",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            } else {
                CompactButton(
                    onClick = { onDeleteClick() },
                    text = "Hapus",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(Modifier.padding(4.dp))

                CompactButton(
                    onClick = { onUpdateClick() },
                    text = "Perbarui",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}


@Composable
fun WalletDetailScreenPreview() {
    SpeechnancialTheme {
        Surface {
            WalletDetailScreen(
                walletName = "Dompet Utama",
                balance = "Rp. 1.000.000",
                keywords = listOf("Gaji", "Bonus", "Investasi"),
                isNewWallet = false,
                onWalletNameClick = {},
                onKeywordEditClick = {},
                onKeywordDeleteClick = {},
                onAddKeywordClick = {},
                onSaveClick = {},
                onUpdateClick = {},
                onDeleteClick = {},
                onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewWalletDetailScreen() {
    WalletDetailScreenPreview()
}