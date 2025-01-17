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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.preview.TransactionItemPreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    isExpanded : Boolean,
    onItemClick: () -> Unit,
    onDropdownClick: () -> Unit,
) {
    Column (
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color =
                    if(!transaction.isValid || transaction.isReviseNeeded) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable { onItemClick() },
    ) {
        if (!transaction.isValid || transaction.isReviseNeeded || transaction.type == TransactionType.UNDEFINED)
            Text(
                text =
                    if (transaction.type == TransactionType.UNDEFINED) "TIPE TRANSAKSI KOSONG"
                    else if (!transaction.isValid) "TIDAK VALID"
                    else "BUTUH REVISI",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    "Rp${formatToThousandsSeparator(transaction.total)}",
                    color =
                    when (transaction.type) {
                        TransactionType.EARNING -> MaterialTheme.colorScheme.tertiaryContainer
                        TransactionType.SPENDING -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =
                    "${transaction.createdAt?.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Icon(
                imageVector =
                    if (isExpanded) Icons.Filled.KeyboardArrowDown
                    else Icons.Filled.KeyboardArrowUp,
                contentDescription = "dropdown icon",
                tint =
                    if(!transaction.isValid || transaction.isReviseNeeded) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.primary,
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(48.dp)
                    .height(48.dp)
                    .padding(
                        bottom = 8.dp
//                        if (!transaction.isValid || transaction.isReviseNeeded || transaction.type == TransactionType.UNDEFINED) 8.dp
//                        else 0.dp
                    )
                    .clickable { onDropdownClick() }
            )
        }

        if (isExpanded) {
            Spacer(Modifier.padding(4.dp))
            transaction.details?.forEach {
                Row {
                    Text(
                        it.description,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp. ${formatToThousandsSeparator(it.nominal)}")
                }
            }
            if (!transaction.isValid) Text(transaction.rawText)
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