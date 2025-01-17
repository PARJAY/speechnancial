package com.example.speechnancial.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.ui.component.inputTransactionScreen.ProposedTransaction
import com.example.speechnancial.ui.component.transactionListScreen.WalletBalanceAndHistoryDisplayer
import com.example.speechnancial.ui.screen.InputTransactionScreen
import com.example.speechnancial.ui.screen.TransactionListScreen
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListState
import com.example.speechnancial.viewmodel.transactionListScreen.WalletQuickHistory

// todo : add remaining preview

@PreviewLightDark
@Composable
fun InputTransactionScreenPreview(
    @PreviewParameter(InputTransactionScreenPreviewParameterProvider::class) state : InputTransactionState
) {
    SpeechnancialTheme {
        val navController = rememberNavController()
        Surface {
            InputTransactionScreen(
                navController,
                state,
                onEvent = {},
                getRecordAudioPermission = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    SpeechnancialTheme {
        TransactionListScreen(
            navController = rememberNavController(),
            state = TransactionListState(),
            onEvent = {},
            onItemClickUpdateData = {}
        )
    }
}

@PreviewLightDark
@Composable
fun TransactionListScreenPreview(
    @PreviewParameter(BallanceInAndOutDisplayerParameterProvider::class) isButtonActive : WalletQuickHistory
) {
    SpeechnancialTheme {
        Surface (color = MaterialTheme.colorScheme.background) {
            Column {
                WalletBalanceAndHistoryDisplayer(
                    isButtonActive.isFilterEarningsActive,
                    onFilterEarningClick = {},
                    isButtonActive.isFilterSpendingActive,
                    onFilterSpendingClick = {},
                    isButtonActive.walletBalanceAndHistory
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
            ProposedTransaction(
                transaction = state.proposedTransaction,
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