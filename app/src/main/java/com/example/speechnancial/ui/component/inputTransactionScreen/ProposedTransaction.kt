package com.example.speechnancial.ui.component.inputTransactionScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.preview.InputTransactionStatePreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import java.text.DecimalFormat

@Composable
fun ProposedTransaction(
    transaction: Transaction,
    isEditExistingTransaction: Boolean,

    onConfirmButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onUpdateButtonClick: () -> Unit,

    isReviseNeeded: Boolean,
    onReviseNeededClick: () -> Unit,

    isTransacribtionError: Boolean,
    onTransacribtionErrorClick: () -> Unit,
) {
    Column (
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // avoiding gap content
        Column {
            Text(
                "id transaksi : ${transaction.id}"
            )
            transaction.details?.forEach {
                Row {
                    Text(
                        it.description,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp${formatToThousandsSeparator(it.nominal)}")
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()  //fill the max height
                .width(1.dp)
        )

        Text(
            "Rp.${formatToThousandsSeparator(transaction.total)}",
            color =
            when (transaction.type) {
                TransactionType.EARNING -> MaterialTheme.colorScheme.tertiaryContainer
                TransactionType.SPENDING -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.primary
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )


        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompactCheckbox(
                isChecked = isReviseNeeded,
                onCheckedChange = { onReviseNeededClick() },
                text = "Revisi Nanti"
            )
            CompactCheckbox(
                isChecked = isTransacribtionError,
                onCheckedChange = { onTransacribtionErrorClick() },
                text = "Kesalahan Transkripsi"
            )
        }

        Row (Modifier.fillMaxWidth()) {
            if(isEditExistingTransaction) {
                CompactButton(
                    onClick = { onDeleteButtonClick() },
                    text = "Hapus",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(Modifier.padding(4.dp))

                CompactButton(
                    onClick = { onUpdateButtonClick() },
                    text = "Perbarui",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }
            else {
                CompactButton(
                    onClick = { onConfirmButtonClick() },
                    text = "Simpan",
                    Modifier.weight(1f),
                    MaterialTheme.colorScheme.tertiary
                )
            }

        }
    }
}


@PreviewLightDark
@Composable
fun ProposedTransactionPreview(
    @PreviewParameter(InputTransactionStatePreviewParameterProvider::class) state: InputTransactionState
) {
    SpeechnancialTheme {
        Surface {
            ProposedTransaction(
                transaction = state.proposedTransaction,
                isEditExistingTransaction = state.isEditExistingTransaction,
                onConfirmButtonClick = { },
                onDeleteButtonClick = { },
                onUpdateButtonClick = { },
                isReviseNeeded = state.isReviseNeeded,
                onReviseNeededClick = { },
                isTransacribtionError = state.isTranscriptionError,
                onTransacribtionErrorClick = { },
            )
        }
    }
}