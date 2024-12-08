package com.example.speechnancial.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.example.speechnancial.presentation.transactionListScreen.TransactionListEvent
import com.example.speechnancial.presentation.transactionListScreen.TransactionListState
import com.example.speechnancial.ui.component.TransactionDisplayerItem
import com.example.speechnancial.ui.component.transactionListScreen.DialogEditTransaction

@Composable
fun TransactionListScreen(
    state: State<TransactionListState>,
    onEvent: (TransactionListEvent) -> Unit
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
                onEvent(TransactionListEvent.UpdateSelectedTransaction)
                onEvent(TransactionListEvent.HideDialog)
            }
        )

    LazyColumn {
        item {
            Text("TransactionListScreen")
        }

        items(state.value.transactionList) { transaction ->
            TransactionDisplayerItem(
                transaction,
                onItemClick = {
                    onEvent(TransactionListEvent.ShowDialog(transaction))
                }
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