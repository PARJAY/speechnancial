package com.example.speechnancial.newUi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.viewmodel.TransactionScreenUiState
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class ChartItemData(val date: String, val totalIncome: Float, val totalOutcome: Float)

@Composable
fun TimeWalletTransactionOverviewItem(
    uiState: TransactionScreenUiState,
    onClickOpenDialogTransactionFilter: () -> Unit
) {
//    val totalBalance = remember { derivedStateOf { uiState.filteredWallet.sumOf { it.balance.toDouble() }.toFloat() } }
//    val totalIncome = remember { derivedStateOf { uiState.filteredWallet.sumOf { it.totalEarning.toDouble() }.toFloat() } }
//    val totalOutcome = remember { derivedStateOf { uiState.filteredWallet.sumOf { it.totalSpending.toDouble() }.toFloat() } }

    val barHeight = 200.dp

    val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault()) // Format tanggal

    val chartItems = remember(uiState.transactions) {
        val groupedTransactions = uiState.transactions.groupBy {
            dateFormat.format(it.dateAdded.toDate())
        }

        val calculatedItems = mutableListOf<ChartItemData>()
        groupedTransactions.forEach { (date, transactions) ->
            var totalIncomeCI = 0f
            var totalOutcomeCI = 0f
            transactions.forEach { transaction ->
                if (
                    transaction.transactionTypeOrdinal == TransactionType.INCOME.ordinal ||
                    transaction.transactionTypeOrdinal == TransactionType.RECEIVABLE_PAYMENT.ordinal
                ) {
                    totalIncomeCI += transaction.total
                } else if (
                    transaction.transactionTypeOrdinal == TransactionType.EXPENSE.ordinal ||
                    transaction.transactionTypeOrdinal == TransactionType.DEBT_PAYMENT.ordinal
                ) {
                    totalOutcomeCI += transaction.total
                }
            }
            calculatedItems.add(ChartItemData(date, totalIncomeCI, totalOutcomeCI))
        }
        calculatedItems
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClickOpenDialogTransactionFilter() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Select Wallet",
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        // DialogPairCode
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Row {
                    Text(
                        text =
                        if(uiState.filteredWallet.size == uiState.wallets.size) "Semua Dompet Terpilih"
                        else if(uiState.filteredWallet.isNotEmpty()) "${uiState.filteredWallet.size} Dompet Terpilih"
                        else "Semua Dompet Terpilih",
                    )
                    Text(
                        text = "Rp ${formatToThousandsSeparator(uiState.totalBalance)}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }

//                Row(
//                    Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = when {
//                            uiState.timeRange != EnumTimeRange.CUSTOM && uiState.timeRange != null -> uiState.timeRange.toString()
//                            uiState.timeRange == EnumTimeRange.CUSTOM && uiState.customDateRange != null -> {
//                                val startDate = uiState.customDateRange.first?.seconds?.toString() ?: "Start"
//                                val endDate = uiState.customDateRange.second?.seconds?.toString() ?: "End"
//                                "$startDate - $endDate"
//                            }
//                            else -> "Time"
//                        }
//                    )
//
//                    Text(
//                        text = "↓ Rp ${formatToThousandsSeparator(uiState.totalIncome)}",
//                        color = MaterialTheme.colorScheme.tertiaryContainer,
//
//                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.End,
//                        modifier = Modifier
//                            .padding(end = 8.dp)
//                            .weight(1f)
//                    )
//                    Text(
//                        text = "↑ Rp ${formatToThousandsSeparator(uiState.totalOutcome)}",
//                        color = MaterialTheme.colorScheme.onTertiaryContainer,
//                        fontWeight = FontWeight.Bold,
//                        textAlign = TextAlign.End,
//                    )
//                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
//            val validTransactions = remember {
//                mutableStateOf(uiState.transactions.filter { transaction ->
//                    transaction.transactionKindOrdinal in listOf(
//                        TransactionKind.REGULAR_EXPENSE.ordinal,
//                        TransactionKind.REGULAR_INCOME.ordinal,
//                        TransactionKind.DEBT_PAYMENT.ordinal,
//                        TransactionKind.RECEIVABLE_PAYMENT.ordinal
//                    )
//                })
//            }
            val validTransactions = uiState.transactions.filter { transaction ->
                    transaction.transactionTypeOrdinal in listOf(
                        TransactionType.EXPENSE.ordinal,
                        TransactionType.INCOME.ordinal,
                        TransactionType.DEBT_PAYMENT.ordinal,
                        TransactionType.RECEIVABLE_PAYMENT.ordinal
                    )
                }

            Column(
                modifier = Modifier.height(barHeight),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.End
            ) {
                val rawHighest = validTransactions.maxOfOrNull { it.total } ?: 0f
//                val rawHighest = validTransactions.value.maxOfOrNull { it.total } ?: 0f
                val highestBarValue = if (rawHighest % 5 == 0f) rawHighest else (rawHighest + (5 - rawHighest % 5))
                val valueStep = if (highestBarValue != 0f) highestBarValue / 5 else 1f

                repeat(6) { i ->
                    Text(
                        text = formatToThousandsSeparator(highestBarValue - i * valueStep),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            LazyRow(
                reverseLayout = true,
                horizontalArrangement = Arrangement.End
            ) {
                items(chartItems) { item ->
                    ChartItem(
                        timeRange = item.date,
                        totalIncome = item.totalIncome,
                        totalOutcome = item.totalOutcome,
//                        highestBarValue = validTransactions.value.maxOfOrNull { it.total } ?: 0f   // ternyata ada yang lain yang menggunakan ini dan belum di revisi
                        highestBarValue = validTransactions.maxOfOrNull { it.total } ?: 0f   // ternyata ada yang lain yang menggunakan ini dan belum di revisi
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TimeWalletTransactionOverviewItemPreview() {
    SpeechnancialTheme {
        Surface {
            TimeWalletTransactionOverviewItem(
                uiState = TransactionScreenUiState(
                    filteredWallet = listOf(
                        Wallet(balance = 100f, totalEarning = 200f, totalSpending = 50f),
                        Wallet(balance = 200f, totalEarning = 300f, totalSpending = 100f)
                    ),
                    transactions = listOf(
                        Transaction(total = 150f, transactionTypeOrdinal = TransactionType.INCOME.ordinal, dateAdded = getTimestampDaysAgo(0)),
                        Transaction(total = 50f, transactionTypeOrdinal = TransactionType.EXPENSE.ordinal, dateAdded = getTimestampDaysAgo(0)),
                        Transaction(total = 200f, transactionTypeOrdinal = TransactionType.INCOME.ordinal, dateAdded = getTimestampDaysAgo(1)),
                        Transaction(total = 100f, transactionTypeOrdinal = TransactionType.EXPENSE.ordinal, dateAdded = getTimestampDaysAgo(1)),
                        Transaction(total = 100f, transactionTypeOrdinal = TransactionType.INCOME.ordinal, dateAdded = getTimestampDaysAgo(2)),
                        Transaction(total = 25f, transactionTypeOrdinal = TransactionType.EXPENSE.ordinal, dateAdded = getTimestampDaysAgo(3)),
                        Transaction(total = 180f, transactionTypeOrdinal = TransactionType.INCOME.ordinal, dateAdded = getTimestampDaysAgo(4)),
                        Transaction(total = 40f, transactionTypeOrdinal = TransactionType.EXPENSE.ordinal, dateAdded = getTimestampDaysAgo(5)),
                        Transaction(total = 220f, transactionTypeOrdinal = TransactionType.INCOME.ordinal, dateAdded = getTimestampDaysAgo(6)),
                        Transaction(total = 70f, transactionTypeOrdinal = TransactionType.EXPENSE.ordinal, dateAdded = getTimestampDaysAgo(7))
                    )
                ),
                onClickOpenDialogTransactionFilter = {}
            )
        }
    }
}

fun getTimestampDaysAgo(daysAgo: Int): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val date = calendar.time
    return Timestamp(date)
}