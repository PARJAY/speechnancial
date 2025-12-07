package com.example.speechnancial.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState

val DUMMY_TRANSACTION_ITEM_STATE_4 = Transaction(
    detailsRoom = listOf(
        TransactionDetail("beli ESP", 120000f),
        TransactionDetail("DHT 11", 20000f),
        TransactionDetail("Breadboard", 100000f),
        TransactionDetail("kabel jumper", 45000f),
    )
)

class InputTransactionScreenPreviewParameterProvider: PreviewParameterProvider<InputTransactionState> {
    override val values: Sequence<InputTransactionState>
        get() = sequenceOf(
            InputTransactionState(),
            InputTransactionState(
                isTranscribing = true
            ),
            InputTransactionState(
                proposedTransaction = DUMMY_TRANSACTION_ITEM_STATE_4,
                source = "beli ESP rp 120000 DHT 11 rp 20000 Breadboard rp 100000 kabel jumper rp 45000",
                isReviseNeeded = true,
                isTranscriptionError = false
            ),
        )
}