package com.example.speechnancial.newUi.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.budgetExample1BelanjaHarian
import com.example.speechnancial.common.budgetExample2GajianMagang
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.tools.Util.Companion.formatTimestampToDayMonth
import com.example.speechnancial.tools.Util.Companion.outcomeOverflowHandler
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import kotlin.math.absoluteValue

@Composable
fun BudgetHistoryItem(
    transactionTypeOrdinal: Int,
    budgetRealization: BudgetRealization,
    budgetBalance: Float,
    onClick: () -> Unit
) {
    // todo : not auto update UI
    val percentage = budgetRealization.progressAmount / budgetBalance
    val progress = percentage.coerceIn(0f, 1f) // Batasan 0-1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(
                BorderStroke(1.dp, Gray),
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .clickable { onClick() }
    ) {
//        Text(budgetRealization.progressAmount)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text =
                    when (transactionTypeOrdinal) {
                        EnumTransactionType.INCOME.ordinal -> "Diterima"
                        EnumTransactionType.OUTCOME.ordinal -> "Terpakai"
                        else -> ""
                    } + " ",
                fontSize = 14.sp,
            )

            Text(
                text = "Rp.${formatToThousandsSeparator(
                    if (budgetRealization.progressAmount >= 0) budgetRealization.progressAmount  
                    else budgetRealization.progressAmount.absoluteValue + budgetBalance
                )}",
                fontSize = 14.sp,
            )

            Text(
                text ="${outcomeOverflowHandler(percentage).toInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = when (transactionTypeOrdinal) {
                    EnumTransactionType.INCOME.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                    EnumTransactionType.OUTCOME.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "${formatTimestampToDayMonth(budgetRealization.startTime)} - ${formatTimestampToDayMonth(budgetRealization.endTime)}",
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        ProgressPercentage(transactionTypeOrdinal, percentage, progress)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BudgetHistoryItemPreview() {
    budgetExample1BelanjaHarian
    budgetExample2GajianMagang
    SpeechnancialTheme {
        Surface {
            Column {
                BudgetHistoryItem(
                    transactionTypeOrdinal = budgetExample1BelanjaHarian.transactionTypeOrdinal,
                    budgetRealization = budgetExample1BelanjaHarian.budgetRealizations[0],
                    budgetBalance = budgetExample1BelanjaHarian.amount,
                    onClick = {}
                )

                BudgetHistoryItem(
                    transactionTypeOrdinal = budgetExample1BelanjaHarian.transactionTypeOrdinal,
                    budgetRealization = budgetExample1BelanjaHarian.budgetRealizations[1],
                    budgetBalance = budgetExample1BelanjaHarian.amount,
                    onClick = {}
                )

                BudgetHistoryItem(
                    transactionTypeOrdinal = budgetExample1BelanjaHarian.transactionTypeOrdinal,
                    budgetRealization = budgetExample1BelanjaHarian.budgetRealizations[2],
                    budgetBalance = budgetExample1BelanjaHarian.amount,
                    onClick = {}
                )

                BudgetHistoryItem(
                    transactionTypeOrdinal = budgetExample2GajianMagang.transactionTypeOrdinal,
                    budgetRealization = budgetExample2GajianMagang.budgetRealizations[0],
                    budgetBalance = budgetExample2GajianMagang.amount,
                    onClick = {}
                )

                BudgetHistoryItem(
                    transactionTypeOrdinal = budgetExample2GajianMagang.transactionTypeOrdinal,
                    budgetRealization = budgetExample2GajianMagang.budgetRealizations[1],
                    budgetBalance = budgetExample2GajianMagang.amount,
                    onClick = {}
                )
            }
        }
    }
}