package com.example.speechnancial.newUi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.speechnancial.common.budgetExample1BelanjaHarian
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.tools.Util.Companion.outcomeOverflowHandler
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun ProgressPercentage(
    transactionTypeOrdinal: Int,
    percentage: Float,
    progress: Float,
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .border(
                    BorderStroke(1.dp, Gray),
                    RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        if (outcomeOverflowHandler(percentage) > 100) 1f
                        else progress
                    )
                    .fillMaxHeight()
                    .background(
                        when (transactionTypeOrdinal) {
                            EnumTransactionType.INCOME.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                            EnumTransactionType.OUTCOME.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                            else -> MaterialTheme.colorScheme.primary
                        },
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}


@PreviewLightDark
@Composable
fun ProgressPercentagePreview() {
    SpeechnancialTheme {
        Surface {
            val budget = budgetExample1BelanjaHarian
            val latestBudgetRealization = budget.budgetRealizations[0]
            val percentage = (latestBudgetRealization.progressAmount / budget.amount)
            val progress = percentage.coerceIn(0f, 1f) // Batasan 0-1

            ProgressPercentage(budget.transactionTypeOrdinal, percentage, progress)
        }
    }
}