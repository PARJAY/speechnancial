package com.example.speechnancial.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionRoomItemState
import java.time.LocalDateTime

class TransactionItemPreviewParameterProvider: PreviewParameterProvider<TransactionRoomItemState> {
    override val values: Sequence<TransactionRoomItemState>
        get() = sequenceOf(
            TransactionRoomItemState(
                Transaction(
                    type = TransactionTypeOld.EARNING,
                    total = 12000f,
                    createdAtRoom = LocalDateTime.now(),
//                    createdAt = Timestamp.now(),
                )
            ),
            TransactionRoomItemState(
                Transaction(
                    type = TransactionTypeOld.UNDEFINED,
                    total = 12000f,
//                    createdAt = Timestamp.now(),
                    createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    isValid = false
                )
            ),
            TransactionRoomItemState(
                Transaction(
                    type = TransactionTypeOld.EARNING,
                    total = 1_000_000f,
                    createdAtRoom = LocalDateTime.now(),
//                    createdAt = Timestamp.now(),
                    detailsRoom = listOf(
                        TransactionDetail("uang bulanan", 1_000_000f),
                    ),
                    isReviseNeeded = true
                ),
                true
            ),
            TransactionRoomItemState(
                Transaction(
                    type = TransactionTypeOld.SPENDING,
                    total = 285_000f,
//                    createdAt = Timestamp.now(),
                    createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    detailsRoom = listOf(
                        TransactionDetail("beli ESP", 120_000f),
                        TransactionDetail("DHT 11", 20_000f),
                        TransactionDetail("Breadboard", 100_000f),
                        TransactionDetail("kabel jumper", 45_000f),
                    ),
                ),
                true
            ),
            TransactionRoomItemState(
                Transaction(
                    type = TransactionTypeOld.UNDEFINED,
                    total = 285_000f,
//                    createdAt = Timestamp.now(),
                    createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                    detailsRoom = listOf(
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