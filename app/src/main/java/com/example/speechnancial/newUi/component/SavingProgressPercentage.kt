package com.example.speechnancial.newUi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.data.firebase.model.DebtType
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun ProgressPercentageWithTextInside(
    collectedAmount: Float,
    targetAmount: Float,
    debtType: DebtType? = null // Menambahkan parameter DebtType (nullable)
) {
    val percentage = if (targetAmount > 0) collectedAmount / targetAmount else 0f
    val progress = percentage.coerceIn(0f, 1f)
    val percentageDisplay = (percentage * 100).toInt()

    val progressColor = when (debtType) {
        DebtType.DEBT -> MaterialTheme.colorScheme.onTertiaryContainer // Merah untuk hutang
        DebtType.RECEIVABLE -> MaterialTheme.colorScheme.tertiaryContainer // Hijau untuk piutang
        else -> MaterialTheme.colorScheme.tertiaryContainer // Default warna jika DebtType null (Saving)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .border(
                    BorderStroke(1.dp, Color.Gray),
                    RoundedCornerShape(8.dp)
                )
                .background(
//                    if (percentage > 1f) progressColor.copy(alpha = 0.45f)
//                    else Color.Transparent
                    Color.Transparent,
                    RoundedCornerShape(8.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
//                        if (percentage > 1f) ((percentage - 1f)).coerceIn(0f, 1f)
//                        else progress
                        progress
                    )
                    .fillMaxHeight()
                    .background(
                        progressColor.copy(alpha = 0.75f),
                        RoundedCornerShape(8.dp)
                    )
            )

            Text(
                text = "Rp.${formatToThousandsSeparator(collectedAmount)}/${formatToThousandsSeparator(targetAmount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterStart)
            )
        }

        Text(
            text = "$percentageDisplay%",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = progressColor,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun ProgressPercentageSavingPreview() {
    SpeechnancialTheme {
        Surface {
            ProgressPercentageWithTextInside(
                collectedAmount = 3000f,
                targetAmount = 5000f
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ProgressPercentageDebtPreview() {
    SpeechnancialTheme {
        Surface {
            ProgressPercentageWithTextInside(
                collectedAmount = 15000f,
                targetAmount = 5000f,
                debtType = DebtType.DEBT
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ProgressPercentageReceivablePreview() {
    SpeechnancialTheme {
        Surface {
            ProgressPercentageWithTextInside(
                collectedAmount = 3000f,
                targetAmount = 5000f,
                debtType = DebtType.RECEIVABLE
            )
        }
    }
}