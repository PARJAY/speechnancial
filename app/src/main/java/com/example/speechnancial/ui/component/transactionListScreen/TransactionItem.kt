package com.example.speechnancial.ui.component.transactionListScreen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.ui.preview.TransactionItemPreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    isExpanded: Boolean,
    onItemClick: () -> Unit,
    onDropdownClick: () -> Unit,
) {
    Column (
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable { onItemClick() },
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    "Rp${transaction.total}",
                    color =
                    when (transaction.type) {
                        TransactionType.INCOME -> MaterialTheme.colorScheme.tertiaryContainer
                        TransactionType.OUTCOME -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(

                    text = transaction.createdAt?.format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Icon(
                imageVector =
                    if (isExpanded) Icons.Filled.KeyboardArrowDown
                    else Icons.Filled.KeyboardArrowUp,
                contentDescription = "dropdown icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(48.dp)
                    .height(48.dp)
                    .clickable { onDropdownClick() }
            )
        }

        if (isExpanded) {
            Spacer(Modifier.padding(16.dp))
            transaction.details?.forEach {
                Row {
                    Text(
                        it.description,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp. ${it.nominal}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TransactionItemPreview(
    @PreviewParameter(TransactionItemPreviewParameterProvider::class) state : TransactionItemState
) {
    SpeechnancialTheme {
        Surface {
            TransactionItem(
                transaction = state.transaction,
                isExpanded = state.isExpanded,
                onItemClick = {},
                onDropdownClick = {}
            )
        }
    }
}