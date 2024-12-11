package com.example.speechnancial.ui.component.transactionListScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.speechnancial.ui.theme.transpernt

@Composable
fun TransactionTypeSummaryButton(
    modifier : Modifier,
    transactionType : Boolean,
    onClick: () -> Unit,
    backgroundColor: Color,
    nominal: String
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = if (transactionType) ButtonColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ) else ButtonColors(
            containerColor = transpernt,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = transpernt,
            disabledContainerColor = transpernt
        ),
        border = if (!transactionType) BorderStroke(
            width = 4.dp,
            color = backgroundColor
        ) else null
    ) {
        Text(nominal)
    }
}