package com.example.speechnancial.newUi.component.sharedComponent

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.common.transaction1
import com.example.speechnancial.common.transaction2
import com.example.speechnancial.common.transaction3
import com.example.speechnancial.common.transaction4
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.tools.Util.Companion.formatTimestampToDayMonth
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme

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
                if(!transaction.isValid || transaction.isNeedRevise) MaterialTheme.colorScheme.onTertiary
                else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable { onItemClick() },
    ) {
        if (!transaction.isValid || transaction.isNeedRevise || transaction.transactionTypeOldOrdinalOld == TransactionTypeOld.UNDEFINED.ordinal)
            Text(
                text =
                if (transaction.transactionTypeOldOrdinalOld == TransactionTypeOld.UNDEFINED.ordinal) "TIPE TRANSAKSI KOSONG"
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
                    when (transaction.transactionTypeOldOrdinalOld) {
                        TransactionTypeOld.EARNING.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                        TransactionTypeOld.SPENDING.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                        // todo : transfer warnanya biru
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // bungkus teks dibawah dengan row, dan tambahkan lagi 1 teks yang berisi category
                Text(
//                    text =
//                    "${transaction.createdAtRoom?.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))}",
                    text = formatTimestampToDayMonth(transaction.dateAdded),
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
                if(!transaction.isValid || transaction.isNeedRevise) MaterialTheme.colorScheme.onTertiary
                else MaterialTheme.colorScheme.primary,
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(48.dp)
                    .height(48.dp)
                    .padding(
                        bottom = 8.dp
//                        if (!transaction.isValid || transaction.isNeedRevise || transaction.type == TransactionType.UNDEFINED) 8.dp
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
                        it.key,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp. ${formatToThousandsSeparator(it.value)}")
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionItemPreview(
) {
    SpeechnancialTheme {
        Surface {
            Column {
                TransactionItem(
                    transaction1,
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    transaction2,
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    transaction3,
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    transaction4,
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
            }
        }
    }
}