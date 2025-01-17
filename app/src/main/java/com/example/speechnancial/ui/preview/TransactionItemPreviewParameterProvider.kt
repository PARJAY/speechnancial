package com.example.speechnancial.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import java.time.LocalDateTime

class TransactionItemPreviewParameterProvider: PreviewParameterProvider<TransactionItemState> {
    override val values: Sequence<TransactionItemState>
        get() = sequenceOf(
            TransactionItemState(
                Transaction(
                    type = TransactionType.EARNING,
                    total = 12000f,
                    createdAt = LocalDateTime.now(),
                )
            ),
            TransactionItemState(
                Transaction(
                    type = TransactionType.UNDEFINED,
                    total = 12000f,
                    createdAt = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    isValid = false
                )
            ),
            TransactionItemState(
                Transaction(
                    type = TransactionType.EARNING,
                    total = 1_000_000f,
                    createdAt = LocalDateTime.now(),
                    details = listOf(
                        TransactionDetail("uang bulanan", 1_000_000f),
                    ),
                    isReviseNeeded = true
                ),
                true
            ),
            TransactionItemState(
                Transaction(
                    type = TransactionType.SPENDING,
                    total = 285_000f,
                    createdAt = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    details = listOf(
                        TransactionDetail("beli ESP", 120_000f),
                        TransactionDetail("DHT 11", 20_000f),
                        TransactionDetail("Breadboard", 100_000f),
                        TransactionDetail("kabel jumper", 45_000f),
                    ),
                ),
                true
            ),
            TransactionItemState(
                Transaction(
                    type = TransactionType.UNDEFINED,
                    total = 285_000f,
                    createdAt = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    details = listOf(
                        TransactionDetail("beli ESP", 120_000f),
                        TransactionDetail("DHT 11", 20_000f),
                        TransactionDetail("Breadboard", 100_000f),
                        TransactionDetail("kabel jumper", 45_000f),
                    ),
                    isValid = false
                ),
                true
            )
        )
}