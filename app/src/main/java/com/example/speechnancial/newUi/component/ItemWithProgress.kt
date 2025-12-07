package com.example.speechnancial.newUi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ItemWithProgress(
    debtAndReceivable: DebtAndReceivable,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .border(
                BorderStroke(1.dp, Gray),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = debtAndReceivable.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(debtAndReceivable.dueDate.toDate()),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ProgressPercentageWithTextInside(
            collectedAmount = debtAndReceivable.paidAmount,
            targetAmount = debtAndReceivable.amount,
            debtType = if (debtAndReceivable.typeOrdinal == DebtType.DEBT.ordinal) DebtType.DEBT else DebtType.RECEIVABLE
        )
    }
}

@PreviewLightDark
@Composable
fun ItemWithProgressSavingPreview() {
    SpeechnancialTheme {
        Surface {
            ItemWithProgress(
                debtAndReceivable = DebtAndReceivable(
                    name = "Dana Pensiun",
                    amount = 5000f,
                    paidAmount = 15000f,
                    dueDate = Timestamp.now(),
                    typeOrdinal = DebtType.RECEIVABLE.ordinal // Gunakan CREDIT untuk Saving (atau sesuaikan)
                ),
                onClick = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ItemWithProgressDebtPreview() {
    SpeechnancialTheme {
        Surface {
            ItemWithProgress(
                debtAndReceivable = DebtAndReceivable(
                    name = "Hutang ke Budi lorem ipsum dolor sit amet",
                    amount = 2000f,
                    paidAmount = 5000f,
                    dueDate = Timestamp.now(),
                    typeOrdinal = DebtType.DEBT.ordinal
                ),
                onClick = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ItemWithProgressReceivablePreview() {
    SpeechnancialTheme {
        Surface {
            ItemWithProgress(
                debtAndReceivable = DebtAndReceivable(
                    name = "Piutang dari Ani",
                    amount = 2500f,
                    paidAmount = 1500f,
                    dueDate = Timestamp.now(),
                    typeOrdinal = DebtType.RECEIVABLE.ordinal
                ),
                onClick = {}
            )
        }
    }
}