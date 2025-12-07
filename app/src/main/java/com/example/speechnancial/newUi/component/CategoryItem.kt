package com.example.speechnancial.newUi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun CategoryItem(
    categoryName: String,
    transactionTypeOrdinal: Int,
    onCategoryItemClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp)
            .clickable {
                onCategoryItemClicked()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = categoryName,
            color = if (transactionTypeOrdinal == TransactionTypeOld.EARNING.ordinal) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@PreviewLightDark
@Composable
fun CategoryItemPreview() {
    SpeechnancialTheme { // Replace with your theme
        Surface {
            Column {
                CategoryItem(
                    categoryName = "Pemasukan Gaji",
                    transactionTypeOrdinal = 1, // Contoh: INCOME
                    onCategoryItemClicked = {}
                )
                CategoryItem(
                    categoryName = "Makan Siang",
                    transactionTypeOrdinal = 2, // Contoh: OUTCOME
                    onCategoryItemClicked = {}
                )
                CategoryItem(
                    categoryName = "Lain-lain",
                    transactionTypeOrdinal = 0, // Contoh: UNDEFINED atau nilai lain tanpa ikon
                    onCategoryItemClicked = {}
                )
            }
        }
    }
}
