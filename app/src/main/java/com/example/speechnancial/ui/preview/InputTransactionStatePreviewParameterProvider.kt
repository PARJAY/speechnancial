package com.example.speechnancial.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import java.time.LocalDateTime

class InputTransactionStatePreviewParameterProvider :
    PreviewParameterProvider<InputTransactionState> {
    override val values: Sequence<InputTransactionState> = sequenceOf(
        InputTransactionState(
            previousPartialResult = "Partial result 1",
            source = "Source text 1",
            proposedTransaction = Transaction(
                type = TransactionTypeOld.EARNING,
                total = 12000f,
                createdAtRoom = LocalDateTime.now(),
                details = mapOf(
                    "beli susu" to 10000f,
                    "bayar parkir" to 2000f,
                )
            ),
            isEditExistingTransaction = true,
            isReviseNeeded = true
        ),
        InputTransactionState(
            previousPartialResult = "Partial result 2",
            source = "Source text 2",
            proposedTransaction = Transaction(
                type = TransactionTypeOld.SPENDING,
                total = 12000f,
                createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0)
            ),
            isTranscriptionError = true
        ),
        InputTransactionState(
            previousPartialResult = "",
            source = "",
            proposedTransaction = Transaction(
                type = TransactionTypeOld.EARNING,
                total = 1_000_000f,
                createdAtRoom = LocalDateTime.now(),
                detailsRoom = listOf(
                    TransactionDetail("uang bulanan", 1_000_000f),
                )
            ),
            isFinishedTranscribing = false,
            isTranscribing = true
        ),
        InputTransactionState(
            previousPartialResult = "",
            source = "",
            proposedTransaction = Transaction(
                type = TransactionTypeOld.SPENDING,
                total = 285_000f,
                createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                detailsRoom = listOf(
                    TransactionDetail("beli ESP", 120_000f),
                    TransactionDetail("DHT 11", 20_000f),
                    TransactionDetail("Breadboard", 100_000f),
                    TransactionDetail("kabel jumper", 45_000f),
                )
            )
        ),
        InputTransactionState()
    )
}