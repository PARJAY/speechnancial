package com.example.speechnancial.newUi.screen.transactionListScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.viewmodel.TransactionScreenEvent
import com.example.speechnancial.data.firebase.viewmodel.TransactionScreenUiState
import com.example.speechnancial.newUi.component.TimeWalletTransactionOverviewItem
import com.example.speechnancial.newUi.component.TransactionItemWithCategory
import com.example.speechnancial.newUi.component.dialogBox.DialogTransactionFilterAndDownloader
import com.example.speechnancial.newUi.component.getTimestampDaysAgo
import com.example.speechnancial.newUi.component.sharedComponent.CompactBottomFAB
import com.example.speechnancial.newUi.component.sharedComponent.Title
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionListScreen(
    transactionScreenUiState: TransactionScreenUiState,
    onEvent: (TransactionScreenEvent) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val dateFormat = remember {
        SimpleDateFormat("dd MMM", Locale.getDefault())
    }

    if (showDialog) {
        DialogTransactionFilterAndDownloader(
            uiState = transactionScreenUiState,
            onWalletSelected = { wallets -> onEvent(TransactionScreenEvent.FilterTransactionByWallet(wallets)) },
            onTimeRangeSelected = { timeRange -> onEvent(TransactionScreenEvent.AdjustGraphByTimeRange(timeRange, transactionScreenUiState.customDateRange)) },
            onCustomDateRangeSelected = { dateRange -> onEvent(TransactionScreenEvent.AdjustGraphByTimeRange(EnumTimeRange.CUSTOM, dateRange)) },
            onSaveSettings = { it ->
                showDialog = false

            },
            onDownloadAllTransactions = { onEvent(TransactionScreenEvent.DownloadTransactionReportCSV(context)) },
            onDismiss = { showDialog = false }
        )
    }

    LazyColumn(Modifier.padding(horizontal = 16.dp)) {
        item {
            TimeWalletTransactionOverviewItem(
                uiState = transactionScreenUiState,
                onClickOpenDialogTransactionFilter = { showDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Title("Daftar Transaksi")

            Spacer(modifier = Modifier.height(8.dp))
        }

        itemsIndexed(transactionScreenUiState.transactions) { index, transaction ->
            val currentDate = dateFormat.format(transaction.dateAdded.toDate())
            val previousDate = transactionScreenUiState.transactions
                .getOrNull(index - 1)
                ?.dateAdded
                ?.toDate()
                ?.let { dateFormat.format(it) }

            if (currentDate != previousDate) {
                Text(
                    text = currentDate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            TransactionItemWithCategory(
                transaction = transaction,
                category =
                if (
                    transaction.transactionTypeOrdinal == TransactionType.EXPENSE.ordinal ||
                    transaction.transactionTypeOrdinal == TransactionType.INCOME.ordinal
                )
                    transactionScreenUiState.categories.find { category ->
                        category.uuid == transaction.relatedEntityId
                    }
                else null,
                onItemClick = {
                    onEvent(
                        TransactionScreenEvent.OnTransactionItemClickOpenInputTransactionScreenWithData(
                            transaction,
                            navHostController
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    CompactBottomFAB(
        icons = Icons.Filled.Add,
        onClick = {
            onEvent(
                TransactionScreenEvent.OnFabClickedOpenInputTransactionScreen(navHostController)
            )
        },
        contentDescription = "Add Transaction"
    )
}


@PreviewLightDark
@Composable
fun TransactionListScreenPreview() {
    val navController = rememberNavController()
    SpeechnancialTheme {
        Surface {
            TransactionListScreen(
                transactionScreenUiState = TransactionScreenUiState(
                    filteredWallet = listOf(
                        Wallet(balance = 100f, totalEarning = 200f, totalSpending = 50f),
                        Wallet(balance = 200f, totalEarning = 300f, totalSpending = 100f)
                    ),
                    transactions = listOf(
                        Transaction(
                            uuid = "transaction-1",
                            total = 100000.0f,
                            dateAdded = Timestamp.now(),
                            transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal,
                            details = mapOf("Item 1" to 50000.0f, "Item 2" to 50000.0f),
                            isValid = true,
                            isNeedRevise = false
                        ),
                        Transaction(total = 50000f, transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal, dateAdded = getTimestampDaysAgo(1)),
                        Transaction(total = 200000f, transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal, dateAdded = getTimestampDaysAgo(2)),
                        Transaction(total = 100000f, transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal, dateAdded = getTimestampDaysAgo(3)),
                        Transaction(total = 100000f, transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal, dateAdded = getTimestampDaysAgo(4)),
                        Transaction(total = 25000f, transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal, dateAdded = getTimestampDaysAgo(5)),
                        Transaction(total = 180000f, transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal, dateAdded = getTimestampDaysAgo(6)),
                        Transaction(total = 40000f, transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal, dateAdded = getTimestampDaysAgo(7)),
                        Transaction(total = 220000f, transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal, dateAdded = getTimestampDaysAgo(8)),
                        Transaction(total = 70000f, transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal, dateAdded = getTimestampDaysAgo(9))
                    )
                ),
                onEvent = {},
                navHostController = navController
            )
        }
    }
}