package com.example.speechnancial.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListEvent
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListState
import com.example.speechnancial.ui.component.transactionListScreen.WalletBalanceAndHistoryDisplayer
import com.example.speechnancial.ui.component.transactionListScreen.DialogEditTransaction
import com.example.speechnancial.ui.component.transactionListScreen.TransactionItem
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun TransactionListScreen(
    state: TransactionListState,
    onEvent: (TransactionListEvent) -> Unit,
) {
    // dialog box composable fun
    if (state.showUpdateTransactionDialog)
        DialogEditTransaction(
            state.selectedTransaction,
            onDismiss = {
                onEvent(TransactionListEvent.HideDialog)
            },
            onUserInput = {
                onEvent(TransactionListEvent.HandleUserInput(it))
            },
            onConfirmUpdate = {
                onEvent(TransactionListEvent.DialogActionUpdateSelectedTransaction)
                onEvent(TransactionListEvent.HideDialog)
            }
        )

    LazyColumn (
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            WalletBalanceAndHistoryDisplayer(
                state.isFilterEarningActive,
                onFilterEarningClick = { onEvent(TransactionListEvent.FilterEarningButtonClick) },
                state.isFilterSpendingActive,
                onFilterSpendingClick = { onEvent(TransactionListEvent.FilterSpendingButtonClick) },
                state.walletBalanceAndHistory
            )

            Spacer(Modifier.padding(16.dp))

            Text(
                "Daftar Transaksi",
                modifier = Modifier.fillMaxWidth(1f),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(Modifier.padding(8.dp))
        }

        items(state.transactionList) { transaction ->
            val isExpandedInternal = remember { mutableStateOf(false) }

            TransactionItem(
                transaction = transaction,
                isExpanded = isExpandedInternal.value,
                onItemClick = {
                    onEvent(TransactionListEvent.TransactionItemOnClickShowDialog
                        (transaction)
                    )
                },
                onDropdownClick = {
                    isExpandedInternal.value = !isExpandedInternal.value
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    SpeechnancialTheme {
        TransactionListScreen(
            state = TransactionListState(),
            onEvent = {}
        )
    }
}