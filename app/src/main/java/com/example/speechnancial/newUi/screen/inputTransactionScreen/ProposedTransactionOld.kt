package com.example.speechnancial.newUi.screen.inputTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.R
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactButton
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactFAB
import com.example.speechnancial.ui.preview.InputTransactionStatePreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState


// banyak hal yang harus di revisi
@Composable
fun ProposedTransactionOld(
    transactions: List<Transaction>,
    isEditExistingTransaction: Boolean,

    onConfirmButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onUpdateButtonClick: () -> Unit,

    isReviseNeeded: Boolean,
    onReviseNeededClick: () -> Unit,

    isTransacribtionError: Boolean,
    onTransacribtionErrorClick: () -> Unit,
) {
    // ini adalah dummy yang sebaiknya ada di preview
    val selectedTransaction : Transaction = Transaction(
        details = mapOf(
            "beli susu" to 10000f,
            "bayar parkir" to 2000f,
        )
    )

    val isReviseNeeded = remember { mutableStateOf(false) }

//    val isSelected = index == currentPosition
//    val dotColor = if (isSelected) MaterialTheme.colors.primary else Color.Gray

    Column (
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Title("Hasil Transaksi")

        Spacer(Modifier.height(2.dp))

        // 4 icon row (transaction kind & type, wallet, category, date)
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(1f)
            ) {
                // jika diklik akan memunculkan dialog DialogTransactionKindPicker
                CompactFAB(
                    painterResources = painterResource(R.drawable.ic_transaction),
                    isActive = true,
                    onClick = {
                    },
                    contentDescription = "mic icon"
                )
                Text(
                    "Jenis Transaksi",
                    textAlign = TextAlign.Center
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(1f)
            ) {
                // jika diklik akan memunculkan DialogDropdownSelect dengan data wallets
                CompactFAB(
                    painterResources = painterResource(R.drawable.ic_wallet),
                    isActive = true,
                    onClick = {
                    },
                    contentDescription = "wallet icon"
                )
                Text(
                    "Dompet Default",
                    textAlign = TextAlign.Center
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(1f)
            ) {
                CompactFAB(
                    // jika diklik akan memunculkan RelatedItemPickerDialog
                    // icon juga menyesuaikan
                    painterResources = painterResource(R.drawable.ic_tag),
                    isActive = false,   // if transaction kind is selected, then active
                    onClick = {
                    },
                    contentDescription = "mic icon"
                )
                Text(
                    // "Kategori/Dompet Asal/Tabungan/Hutang Piutang"
                    "Kategori",
                    textAlign = TextAlign.Center
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.weight(1f)
            ) {
                CompactFAB(
                    Icons.Default.DateRange,
                    isActive = true,
                    onClick = {
                    },
                    contentDescription = "calendar icon"
                )
                Text(
                    "Hari ini",
                    textAlign = TextAlign.Center
                )
            }

//            Column (
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Top,
//                modifier = Modifier.weight(1f)
//            ) {
//                CompactFAB(
//                    painterResources = painterResource(
//                        if (isReviseNeeded.value) R.drawable.ic_marked
//                        else R.drawable.ic_not_marked
//                    ),
//                    isActive = isReviseNeeded.value,
//                    onClick = {
//                        !isReviseNeeded.value
//                    },
//                    contentDescription = "revise needed icon"
//                )
//                Text(
//                    "Butuh Revisi?",
//                    textAlign = TextAlign.Center
//                )
//            }
        }

        Column {
            selectedTransaction.details?.entries?.forEachIndexed { index, it ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        it.key,
                        modifier = Modifier.weight(1f)
                    )

                    Text("Rp${formatToThousandsSeparator(it.value)}")

                    Spacer(Modifier.width(8.dp))

                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp) // Add padding around dots
//                            .size(8.dp) // Small dot size
                                .clip(CircleShape) // Make it a circle
                                .background(
                                    MaterialTheme.colorScheme.tertiary
                                )
                                .clickable { /*onDotClick(index)*/ }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "tambah transaksi baru",
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.padding(top = 8.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()  //fill the max height
                .width(1.dp)
        )

        Text(
            "Rp.${formatToThousandsSeparator(selectedTransaction.total)}",
            color =
            when (selectedTransaction.type) {
                TransactionTypeOld.EARNING -> MaterialTheme.colorScheme.tertiaryContainer
                TransactionTypeOld.SPENDING -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.primary
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        // transaction size and current transaction position dot indicator
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.width(8.dp))

            repeat(transactions.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp) // Add padding around dots
                        .size(12.dp) // Small dot size
                        .clip(CircleShape) // Make it a circle
                        .background(
//                            if (active) Color.White
//                            else Color.Gray
                            Color.Gray
                        )
                        .clickable { /*onDotClick(index)*/ }
                )

                Spacer(Modifier.width(8.dp))
            }
        }

        Row (Modifier.fillMaxWidth()) {
            if(isEditExistingTransaction) {
                CompactButton(
                    onClick = { onDeleteButtonClick() },
                    text = "Hapus",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(Modifier.padding(4.dp))

                CompactButton(
                    onClick = { onUpdateButtonClick() },
                    text = "Perbarui",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }
            else {
                CompactButton(
                    onClick = { onConfirmButtonClick() },
                    text = "Simpan",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
fun ProposedTransactionPreview(
    @PreviewParameter(InputTransactionStatePreviewParameterProvider::class) state: InputTransactionState
) {
    SpeechnancialTheme {
        Surface {
            ProposedTransactionOld(
                transactions = listOf(Transaction(), Transaction(), Transaction()),
                isEditExistingTransaction = state.isEditExistingTransaction,
                onConfirmButtonClick = { },
                onDeleteButtonClick = { },
                onUpdateButtonClick = { },
                isReviseNeeded = state.isReviseNeeded,
                onReviseNeededClick = { },
                isTransacribtionError = state.isTranscriptionError,
                onTransacribtionErrorClick = { },
            )
        }
    }
}