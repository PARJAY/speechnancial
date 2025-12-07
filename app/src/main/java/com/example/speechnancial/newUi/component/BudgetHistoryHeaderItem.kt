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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun BudgetHistoryHeaderItem(
    budget: Budget,
    onClick: () -> Unit
) {
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

            Text(
                text = "Rp.${formatToThousandsSeparator(budget.amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            Text(
                text = "${budget.involvedCategoriesUuid?.size ?: 0} Kategori Termasuk",
                fontSize = 14.sp
            )

            Text(
                text = when (budget.recurringTypeOrdinal) {
                    EnumTimeRange.DAILY.ordinal -> "Harian"
                    EnumTimeRange.WEEKLY.ordinal -> "Mingguan"
                    EnumTimeRange.MONTHLY.ordinal -> "Bulanan"
                    EnumTimeRange.YEARLY.ordinal -> "Tahunan"
                    else -> "${budget.timeRangeInDays} hari"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
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
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun BudgetHistoryHeaderItemPreview() {
    SpeechnancialTheme {
        Surface {
            BudgetHistoryHeaderItem(
                budgetExample1BelanjaHarian,
                onClick = {}
            )
        }
    }
}