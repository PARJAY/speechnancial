package com.example.speechnancial.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.speechnancial.ui.component.inputTransactionScreen.ProposedTransaction
import com.example.speechnancial.ui.component.transactionListScreen.WalletBalanceAndHistoryDisplayer
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import com.example.speechnancial.viewmodel.transactionListScreen.WalletQuickHistory

// todo : add remaining preview

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

// todo : dirty and duplicate code, revise later
@PreviewLightDark
@Composable
fun ProposedTransactionPreview(
    @PreviewParameter(TransactionItemPreviewParameterProvider::class) state : TransactionItemState
) {
    SpeechnancialTheme {
        Surface {
            ProposedTransaction(
                transaction = state.transaction,
                isEditExistingTransaction = true,

                onConfirmButtonClick = {},
                onDeleteButtonClick = {},

                isReviseNeeded = true,
                onReviseNeededClick = {},
                isTransacribtionError = false,
                onTransacribtionErrorClick = {},
            )
        }
    }
}

// todo : dirty and duplicate code, revise later
@PreviewLightDark
@Composable
fun ProposedTransactionPreview2(
    @PreviewParameter(TransactionItemPreviewParameterProvider::class) state : TransactionItemState
) {
    SpeechnancialTheme {
        Surface {
            ProposedTransaction(
                transaction = state.transaction,
                isEditExistingTransaction = false,

                onConfirmButtonClick = {},
                onDeleteButtonClick = {},

                isReviseNeeded = false,
                onReviseNeededClick = {},
                isTransacribtionError = true,
                onTransacribtionErrorClick = {},
            )
        }
    }
}