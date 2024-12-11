package com.example.speechnancial.ui.screen

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.example.speechnancial.data.model.WalletBalanceAndHistory
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListEvent
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListState
import com.example.speechnancial.ui.component.TransactionDisplayerItem
import com.example.speechnancial.ui.component.transactionListScreen.WalletBalanceAndHistoryDisplayer
import com.example.speechnancial.ui.component.transactionListScreen.DialogEditTransaction
import com.example.speechnancial.ui.component.transactionListScreen.TransactionItem

@Composable
fun TransactionListScreen(
    state: State<TransactionListState>,
    onEvent: (TransactionListEvent) -> Unit,
) {
    // dialog box composable fun
    if (state.value.showUpdateTransactionDialog)
        DialogEditTransaction(
            state.value.selectedTransaction,
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

    LazyColumn {
        item {
            WalletBalanceAndHistoryDisplayer(
                false,
                onFilterEarningClick = { onEvent(TransactionListEvent.FilterEarningButtonClick) },
                false,
                onFilterSpendingClick = { onEvent(TransactionListEvent.FilterSpendingButtonClick) },
                WalletBalanceAndHistory()
            )
        }

        items(state.value.transactionList) { transaction ->
            TransactionItem(
                transaction = transaction.transaction,
                isExpanded = transaction.isExpanded,
                onItemClick = {},
                onDropdownClick = {}
            )
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun TransactionListScreenPreview() {
//    SpeechnancialTheme {
//        TransactionListScreen()
//    }
//}