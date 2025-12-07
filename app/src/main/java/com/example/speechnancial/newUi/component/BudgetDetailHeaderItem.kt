package com.example.speechnancial.newUi.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.tools.Util.Companion.formatTimestampToDayMonth
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import kotlin.math.absoluteValue

// todo: revisi salah logikanya ni
@Composable
fun BudgetDetailHeaderItem(
    budget: Budget,
    onClick: () -> Unit
) {
    val latestBudgetRealization = budget.budgetRealizations.last()
    val percentage = (latestBudgetRealization.progressAmount / budget.amount)
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
            .clickable {
                onClick()
            }
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Column {
                Row {
                    Text(
                        text = budget.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Budget",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = "${budget.involvedCategoriesUuid?.size ?: 0} Kategori Termasuk",
                    fontSize = 12.sp
                )
            }

            Text(
                text = when (budget.recurringTypeOrdinal) {
                    EnumTimeRange.DAILY.ordinal -> "Harian"
                    EnumTimeRange.WEEKLY.ordinal -> "Mingguan"
                    EnumTimeRange.MONTHLY.ordinal -> "Bulanan"
                    EnumTimeRange.YEARLY.ordinal -> "Tahunan"
                    else -> "${formatTimestampToDayMonth(latestBudgetRealization.startTime)} - ${formatTimestampToDayMonth(latestBudgetRealization.endTime)}"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )

            if (budget.recurringTypeOrdinal != EnumTimeRange.NOT_RECURRING.ordinal) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Recurring",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rp.${formatToThousandsSeparator(latestBudgetRealization.progressAmount.absoluteValue)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = when (budget.transactionTypeOrdinal) {
                    EnumTransactionType.INCOME.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                    EnumTransactionType.OUTCOME.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            Text(
                text = " " +
                        if (percentage < 0 || percentage > 100) "Terlampaui"
                        else if (budget.transactionTypeOrdinal == EnumTransactionType.INCOME.ordinal) "Diterima"
                        else if (budget.transactionTypeOrdinal == EnumTransactionType.OUTCOME.ordinal) "Tersisa"
                        else "",
                fontSize = 12.sp,
            )

            Text(
                text = " dari Rp.${formatToThousandsSeparator(budget.amount)}",
                fontSize = 12.sp,
            )
        }

        ProgressPercentage(budget.transactionTypeOrdinal, percentage, progress)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BudgetDetailHeaderItemPreview() {
    SpeechnancialTheme {
        Surface {
            BudgetDetailHeaderItem(
                budgetExample1BelanjaHarian,
                onClick = {}
            )
        }
    }
}