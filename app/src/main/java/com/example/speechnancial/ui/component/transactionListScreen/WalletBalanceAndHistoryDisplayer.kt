package com.example.speechnancial.ui.component.transactionListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.R
import com.example.speechnancial.viewmodel.transactionListScreen.WalletQuickHistory
import com.example.speechnancial.data.model.WalletBalanceAndHistory
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.preview.BallanceInAndOutDisplayerParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.ui.theme.transpernt

@Composable
fun WalletBalanceAndHistoryDisplayer(
    isIncomeActive : Boolean,
    onFilterEarningClick: () -> Unit,
    isOutcomeActive : Boolean,
    onFilterSpendingClick: () -> Unit,
    walletBalanceAndHistory: WalletBalanceAndHistory
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                tint = transpernt,
                contentDescription = "app setting",
            )
            Text(
                "Balance",
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Icon(
                modifier = Modifier.clickable { /* TODO on event setting click VM */ },
                painter = painterResource(R.drawable.ic_settings),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "app setting"
            )
        }
        Text(
            "Rp. ${formatToThousandsSeparator(walletBalanceAndHistory.currentBalance)}",
            modifier = Modifier.fillMaxWidth(1f),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Row (
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TransactionTypeSummaryButton(
                modifier = Modifier.weight(1f),
                onClick = { onFilterEarningClick() },
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                transactionType = isIncomeActive,
                nominal = "+ Rp. ${formatToThousandsSeparator(walletBalanceAndHistory.totalEarnings)}"
            )

            TransactionTypeSummaryButton(
                modifier = Modifier.weight(1f),
                onClick = { onFilterSpendingClick() },
                backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer,
                transactionType = isOutcomeActive,
                nominal = "- Rp. ${formatToThousandsSeparator(walletBalanceAndHistory.totalSpending)}"
            )
        }
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