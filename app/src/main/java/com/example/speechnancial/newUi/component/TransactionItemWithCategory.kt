package com.example.speechnancial.newUi.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.R
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.tools.Util.Companion.formatTimestampToHourMinute
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp

// todo : pengembangan tampilin semua data di baris text waktunya, bisa kok itu

@Composable
fun TransactionItemWithCategory(
    transaction: Transaction,
    category: Category?,
    onItemClick: () -> Unit,
    isExpanded: Boolean = false
) {
    var internalIsExpanded by remember { mutableStateOf(isExpanded) }

    Column(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (!transaction.isValid || transaction.isNeedRevise) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable { onItemClick() },
    ) {
        if (
            transaction.transactionTypeOrdinal == TransactionType.UNDEFINED.ordinal ||
            !transaction.isValid ||
            transaction.isFromSmartwatch
        )
            Text(
                text =
                    if (transaction.isFromSmartwatch) "INPUT DARI SMARTWATCH"
                    else if (transaction.transactionTypeOrdinal == TransactionType.UNDEFINED.ordinal) "TIPE TRANSAKSI KOSONG"
                    else "TIDAK VALID",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
            )

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(
                    when (transaction.transactionTypeOrdinal) {
                        TransactionType.EXPENSE.ordinal, TransactionType.INCOME.ordinal -> R.drawable.ic_transaction
                        TransactionType.SAVING_DEPOSIT.ordinal, TransactionType.SAVING_WITHDRAWAL.ordinal -> R.drawable.ic_saving
                        TransactionType.REGULAR_TRANSFER.ordinal -> R.drawable.ic_wallet
                        TransactionType.DEBT_PAYMENT.ordinal, TransactionType.RECEIVABLE_PAYMENT.ordinal -> R.drawable.ic_dept_receivable_2
                        else -> R.drawable.ic_tag
                    }),
                contentDescription = "dropdown icon",
                tint = if (!transaction.isValid || transaction.isNeedRevise) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .clickable { internalIsExpanded = !internalIsExpanded }
            )

            Spacer(Modifier.width(8.dp))


            Column (modifier = Modifier.weight(1f)) {
                Text(
                    "Rp${formatToThousandsSeparator(transaction.total)}",
//                    color = when (transaction.transactionTypeOrdinal) {
//                        TransactionType.EARNING.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
//                        TransactionType.SPENDING.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
//                        else -> MaterialTheme.colorScheme.primary
//                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = when (transaction.transactionTypeOrdinal) {
                        TransactionType.INCOME.ordinal, TransactionType.RECEIVABLE_PAYMENT.ordinal -> MaterialTheme.colorScheme.tertiaryContainer
                        TransactionType.EXPENSE.ordinal, TransactionType.DEBT_PAYMENT.ordinal -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.tertiary  // the rest saving and transfer, go into blue color
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                    formatTimestampToHourMinute(transaction.dateAdded) +
                    " " +
                    if (
                        transaction.transactionTypeOrdinal == TransactionType.EXPENSE.ordinal ||
                        transaction.transactionTypeOrdinal == TransactionType.INCOME.ordinal
                    ) category?.let { " - ${it.name}" } ?: "- Kategori kosong"
                    else "",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Icon(
                imageVector = if (internalIsExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                contentDescription = "dropdown icon",
                tint = if (!transaction.isValid || transaction.isNeedRevise) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(48.dp)
                    .height(48.dp)
                    .clickable { internalIsExpanded = !internalIsExpanded }
            )
        }

        if (internalIsExpanded) {
            transaction.details?.forEach {
                Row {
                    Text(it.key, modifier = Modifier.weight(1f))
                    Text("Rp. ${formatToThousandsSeparator(it.value)}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemWithCategoryPreview() {
    val transaction = Transaction(
        uuid = "transaction-1",
        total = 100000.0f,
        dateAdded = Timestamp.now(),
        transactionTypeOldOrdinalOld = TransactionTypeOld.SPENDING.ordinal,
        transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
        details = mapOf("Item 1" to 50000.0f, "Item 2" to 50000.0f),
        isValid = true,
        isNeedRevise = false
    )
    val category = Category(uuid = "category-1", name = "Makanan")

    TransactionItemWithCategory(
        transaction = transaction,
        category = category,
        onItemClick = {},
        isExpanded = true
    )
}

@PreviewLightDark
@Composable
fun TransactionItemWithCategoryNoCategoryPreview() {
    val transaction = Transaction(
        uuid = "transaction-2",
        total = 500000.0f,
        dateAdded = Timestamp.now(),
        transactionTypeOldOrdinalOld = TransactionTypeOld.EARNING.ordinal,
        transactionTypeOrdinal = TransactionType.INCOME.ordinal,
        details = mapOf("Gaji" to 500000.0f),
        isValid = true,
        isNeedRevise = false
    )

    SpeechnancialTheme {
        Surface {
            TransactionItemWithCategory(
                transaction = transaction,
                category = null,
                onItemClick = {},
                isExpanded = false
            )
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionItemWithCategoryInvalidPreview() {
    val transaction = Transaction(
        uuid = "transaction-3",
        total = 200000.0f,
        dateAdded = Timestamp.now(),
        transactionTypeOldOrdinalOld = TransactionTypeOld.UNDEFINED.ordinal,
        transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
        details = null,
        isValid = false,
        isNeedRevise = false
    )
    val category = Category(uuid = "category-3", name = "Lain-lain")

    SpeechnancialTheme {
        Surface {
            TransactionItemWithCategory(
                transaction = transaction,
                category = category,
                onItemClick = {},
                isExpanded = true
            )
        }
    }
}

@PreviewLightDark
@Composable
fun TransactionItemWithUndefinedAndCategoryInvalidPreview() {
    val transaction = Transaction(
        uuid = "transaction-3",
        total = 200000.0f,
        dateAdded = Timestamp.now(),
        transactionTypeOldOrdinalOld = TransactionTypeOld.UNDEFINED.ordinal,
        transactionTypeOrdinal = TransactionType.UNDEFINED.ordinal,
        details = null,
        isValid = false,
        isNeedRevise = false
    )
    val category = Category(uuid = "category-3", name = "Lain-lain")

    SpeechnancialTheme {
        Surface {
            TransactionItemWithCategory(
                transaction = transaction,
                category = category,
                onItemClick = {},
                isExpanded = true
            )
        }
    }
}