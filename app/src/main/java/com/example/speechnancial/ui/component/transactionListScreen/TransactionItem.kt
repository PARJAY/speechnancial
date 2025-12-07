package com.example.speechnancial.ui.component.transactionListScreen

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
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.tools.Util.Companion.formatTimestampToDayMonth
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.time.LocalDateTime

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
        if (!transaction.isValid || transaction.isReviseNeeded || transaction.type == TransactionTypeOld.UNDEFINED)
            Text(
                text =
                    if (transaction.type == TransactionTypeOld.UNDEFINED) "TIPE TRANSAKSI KOSONG"
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
                        TransactionTypeOld.EARNING -> MaterialTheme.colorScheme.tertiaryContainer
                        TransactionTypeOld.SPENDING -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.primary
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
//                    text =
//                    "${transaction.createdAtRoom?.format(DateTimeFormatter.ofPattern("dd MMMM HH:mm"))}",
                    text = formatTimestampToDayMonth(transaction.createdAt ?: Timestamp.now()),
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
            /*  old way (room model)
//            transaction.detailsRoom?.forEach {
//                Row {
//                    Text(
//                        it.description,
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text("Rp. ${formatToThousandsSeparator(it.nominal)}")
//                }
//            }
 */
            transaction.details?.forEach {
                Row {
                    Text(
                        it.key,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp. ${formatToThousandsSeparator(it.value)}")
                }
            }
//            if (!transaction.isValid) Text(transaction.rawText)
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionItemPreview(
//    @PreviewParameter(TransactionItemPreviewParameterProvider::class) state : TransactionItemState
) {
    SpeechnancialTheme {
        Surface {
            Column {
                TransactionItem(
                    transaction =
                    Transaction(
                        type = TransactionTypeOld.EARNING,
                        total = 12000f,
                        createdAtRoom = LocalDateTime.now(),
                        createdAt = Timestamp.now(),
                    ),
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    Transaction(
                        type = TransactionTypeOld.UNDEFINED,
                        total = 12000f,
                        createdAt = Timestamp.now(),
                        createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                        isValid = false
                    ),
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    Transaction(
                        type = TransactionTypeOld.EARNING,
                        total = 1_000_000f,
                        createdAtRoom = LocalDateTime.now(),
                        createdAt = Timestamp.now(),
                        detailsRoom = listOf(
                            TransactionDetail("uang bulanan", 1_000_000f),
                        ),
                        details = mapOf("uang bulanan" to 1_000_000f),
                        isReviseNeeded = true
                    ),
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    Transaction(
                        type = TransactionTypeOld.SPENDING,
                        total = 285_000f,
                        createdAt = Timestamp.now(),
                        createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                        detailsRoom = listOf(
                            TransactionDetail("beli ESP", 120_000f),
                            TransactionDetail("DHT 11", 20_000f),
                            TransactionDetail("Breadboard", 100_000f),
                            TransactionDetail("kabel jumper", 45_000f),
                        ),
                        details = mapOf(
                            "beli ESP" to 120_000f,
                            "DHT 11" to 20_000f,
                            "Breadboard" to 100_000f,
                            "kabel jumper" to 45_000f,
                        ),
                    ),
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
                TransactionItem(
                    Transaction(
                        type = TransactionTypeOld.UNDEFINED,
                        total = 285_000f,
                        createdAt = Timestamp.now(),
                        createdAtRoom = LocalDateTime.of(2024, 3, 20, 20, 13, 0),
                        detailsRoom = listOf(
                            TransactionDetail("beli ESP", 120_000f),
                            TransactionDetail("DHT 11", 20_000f),
                            TransactionDetail("Breadboard", 100_000f),
                            TransactionDetail("kabel jumper", 45_000f),
                        ),
                        details = mapOf(
                            "beli ESP" to 120_000f,
                            "DHT 11" to 20_000f,
                            "Breadboard" to 100_000f,
                            "kabel jumper" to 45_000f,
                        ),
                        isValid = false
                    ),
                    isExpanded = true,
                    onItemClick = {},
                    onDropdownClick = {}
                )
//            TransactionItem(
//                transaction = state.transaction,
//                isExpanded = true,
//                onItemClick = {},
//                onDropdownClick = {}
//            )
            }
        }
    }
}