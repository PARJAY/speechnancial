package com.example.speechnancial.newUi.component

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.R
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.viewmodel.InputTransactionEvent
import com.example.speechnancial.data.firebase.viewmodel.ProposedTransactionUiState
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactButton
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactFAB
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ProposedTransaction(
    proposedTransactions: List<ProposedTransactionUiState>,
    isEditExistingTransaction: Boolean,
    onConfirmButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onUpdateButtonClick: () -> Unit,

    currentPosition: Int,
    onIsNeedReviseChanged: (Boolean) -> Unit,
    isNeedRevise: Boolean,

    onEvent: (InputTransactionEvent) -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Hasil Transaksi", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(2.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                CompactFAB(
                    painterResources = painterResource(R.drawable.ic_transaction),
                    isActive = proposedTransactions[currentPosition].transaction.transactionTypeOrdinal != TransactionType.UNDEFINED.ordinal,       // this is the way to handle it
                    onClick = { onEvent(InputTransactionEvent.OpenTransactionKindDialog) },
                    contentDescription = "transaction kind icon"
                )

                Text(
                    text = when (proposedTransactions[currentPosition].transaction.transactionTypeOrdinal) {
                        TransactionType.EXPENSE.ordinal -> "Pengeluaran"
                        TransactionType.INCOME.ordinal -> "Pemasukan"
                        TransactionType.REGULAR_TRANSFER.ordinal -> "Transfer"
                        TransactionType.SAVING_DEPOSIT.ordinal -> "Setor Tabungan"
                        TransactionType.SAVING_WITHDRAWAL.ordinal -> "Tarik Tabungan"
                        TransactionType.DEBT_PAYMENT.ordinal -> "Pembayaran Hutang"
                        TransactionType.RECEIVABLE_PAYMENT.ordinal -> "Pembayaran Piutang"
                        else -> "Jenis Transaksi"
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                CompactFAB(
                    painterResources = painterResource(R.drawable.ic_wallet),
                    isActive = proposedTransactions[currentPosition].walletName?.isNotEmpty() ?: false,
                    onClick = { onEvent(InputTransactionEvent.OpenSelectWalletDialog) },
                    contentDescription = "wallet icon"
                )
                Text(proposedTransactions[currentPosition].walletName ?: "Pilih Dompet", textAlign = TextAlign.Center, fontSize = 14.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                CompactFAB(
                    painterResources = painterResource(
                        when (proposedTransactions[currentPosition].transaction.transactionTypeOrdinal) {
                            TransactionType.EXPENSE.ordinal, TransactionType.INCOME.ordinal -> R.drawable.ic_tag
                            TransactionType.SAVING_DEPOSIT.ordinal, TransactionType.SAVING_WITHDRAWAL.ordinal -> R.drawable.ic_saving
                            TransactionType.REGULAR_TRANSFER.ordinal -> R.drawable.ic_wallet
                            TransactionType.DEBT_PAYMENT.ordinal, TransactionType.RECEIVABLE_PAYMENT.ordinal -> R.drawable.ic_dept_receivable_2
                            else -> R.drawable.ic_tag
                        }),
                    isActive = proposedTransactions[currentPosition].relatedItemKindName?.isNotEmpty() ?: false,
                    onClick = { onEvent(InputTransactionEvent.OpenRelatedItemDialog) }, // Gunakan onEvent
                    contentDescription = "category icon"
                )

                Text(
                    text =
                    if (proposedTransactions[currentPosition].transaction.transactionTypeOrdinal != TransactionType.UNDEFINED.ordinal)
                        proposedTransactions[currentPosition].relatedItemKindName ?: "Pilih Item"
                    else "Pilih Item",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                CompactFAB(
                    Icons.Default.DateRange,
                    isActive = true,
                    onClick = { onEvent(InputTransactionEvent.OpenDatePickerDialog) }, // Gunakan onEvent
                    contentDescription = "calendar icon"
                )
                Text(formattedDateText(proposedTransactions[currentPosition].transaction.dateAdded), textAlign = TextAlign.Center, fontSize = 14.sp)
//                Text("${proposedTransactions[currentPosition].transaction.dateAdded.seconds} ${proposedTransactions[currentPosition].transaction.dateAdded.nanoseconds}")
            }
        }

        Spacer(Modifier.height(8.dp))

        proposedTransactions[currentPosition].let { proposedTransaction ->
            Column {
                proposedTransaction.transaction.details?.entries?.forEachIndexed { index, (key, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(key, modifier = Modifier.weight(1f))
                        Text("Rp${formatToThousandsSeparator(value)}")
                        Spacer(Modifier.width(8.dp))
                        if (index > 0) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .clickable {
                                        onEvent(InputTransactionEvent.ChangeCurrentTransactionPosition(index)) // Call event
                                    }
                            ) {
                                Icon(Icons.Filled.Add, tint = MaterialTheme.colorScheme.primary, contentDescription = "add transaction")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.padding(top = 8.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth().width(1.dp))

        if (proposedTransactions[currentPosition].transaction.details != null) {
            Text(
                "Rp.${formatToThousandsSeparator(proposedTransactions[currentPosition].transaction.total)}",
                color = when (proposedTransactions[currentPosition].transaction.transactionTypeOrdinal) {
                    TransactionType.INCOME.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                    TransactionType.RECEIVABLE_PAYMENT.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                    TransactionType.SAVING_WITHDRAWAL.ordinal -> MaterialTheme.colorScheme.tertiaryContainer

                    TransactionType.EXPENSE.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                    TransactionType.DEBT_PAYMENT.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                    TransactionType.SAVING_DEPOSIT.ordinal -> MaterialTheme.colorScheme.tertiaryContainer

                    TransactionType.REGULAR_TRANSFER.ordinal -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Spacer(Modifier.width(8.dp))
            repeat(proposedTransactions.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (index == currentPosition) MaterialTheme.colorScheme.primary else Color.Gray)
                        .clickable {
                            onEvent(InputTransactionEvent.ChangeCurrentTransactionPosition(index)) // Panggil event saat dot diklik
                        }
                )
                Spacer(Modifier.width(8.dp))
            }
        }

        Row(Modifier.fillMaxWidth()) {
            if (isEditExistingTransaction) {
                CompactButton(
                    onClick = onDeleteButtonClick,
                    text = "Hapus",
                    Modifier.weight(1f), MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(Modifier.padding(4.dp))
                CompactButton(
                    onClick = {
                        if (InputChecker(proposedTransactions, context)) onUpdateButtonClick()
                    },
                    text = "Perbarui",
                    Modifier.weight(1f), MaterialTheme.colorScheme.tertiary
                )
            } else {
                CompactButton(
                    onClick = {
                        if (InputChecker(proposedTransactions, context)) onConfirmButtonClick()
                    },
                    text = "Simpan",
                    Modifier.weight(1f), MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

fun InputChecker (proposedTransactions: List<ProposedTransactionUiState>, context: Context) : Boolean {
    var passedToAction = true

    for (transaction in proposedTransactions) {
        if (
            transaction.relatedItemKindName.isNullOrEmpty() ||
            transaction.relatedItemKindName.isEmpty()
        ) {
            showToast(context, "Pastikan Semua Input Terisi!")
            passedToAction = false
            break
        }
    }
    return passedToAction
}

// Fungsi untuk menampilkan Toast
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun formattedDateText(selectedDate: Timestamp?): String {
    return selectedDate?.let {
        val instant = Instant.ofEpochSecond(it.seconds, it.nanoseconds.toLong() / 1000)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID")) // Nama hari dalam Bahasa Indonesia
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM", Locale.getDefault())
        val formattedDate = zonedDateTime.format(dateFormatter)
        "$dayOfWeek $formattedDate"
    } ?: "Tanggal"
}

@PreviewLightDark
@Composable
fun ProposedTransactionPreview() {
    val isNeedReviseState = remember { mutableStateOf(false) }

    SpeechnancialTheme {
        Surface {
            ProposedTransaction(
                proposedTransactions = listOf(
                    ProposedTransactionUiState(
                        Transaction(
                            details = mapOf("beli susu" to 10000f, "bayar parkir" to 2000f),
                            total = 12000f,
                            transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal,
                            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
                            relatedEntityId = "uuid to Makanan category"
                        ),
                        "Dompet Utama",
                        ""
                    ),
                    ProposedTransactionUiState(
                        Transaction(
                            details = mapOf("gaji" to 5000000f),
                            total = 5000000f,
                            transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal,
                            transactionTypeOrdinal = TransactionType.INCOME.ordinal,
                            relatedEntityId = "uuid to Gajian category"
                        ),
                        "Dompet M-BCA",
                        ""
                    ),
                ),
                isEditExistingTransaction = false,
                onConfirmButtonClick = {},
                onDeleteButtonClick = {},
                onUpdateButtonClick = {},
                onEvent = {},
                currentPosition = 0,
                onIsNeedReviseChanged = { isNeedReviseState.value = it },
                isNeedRevise = isNeedReviseState.value
            )
        }
    }
}