package com.example.speechnancial.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.speechnancial.data.model.WalletBalanceAndHistory
import com.example.speechnancial.viewmodel.transactionListScreen.WalletQuickHistory

class BallanceInAndOutDisplayerParameterProvider: PreviewParameterProvider<WalletQuickHistory> {
    override val values: Sequence<WalletQuickHistory>
        get() = sequenceOf(
            WalletQuickHistory(false, false, WalletBalanceAndHistory(250000f, 250000f, 500000f)),
            WalletQuickHistory(false, true, WalletBalanceAndHistory(500000f, 100000f, 600000f)),
            WalletQuickHistory(true, false, WalletBalanceAndHistory(-20000f, 30000f, 10000f)),
            WalletQuickHistory(true, true, WalletBalanceAndHistory(-35000f, 50000f, 15000f)),
        )
}