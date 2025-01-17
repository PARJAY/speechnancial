package com.example.speechnancial.ui.component.inputTransactionScreen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.ui.theme.transpernt

@Composable
fun CompactButton(onClick: () -> Unit, text: String, modifier: Modifier, backgroundColor: Color) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = transpernt,
            disabledContainerColor = transpernt
        ),

    ) {

        Text(
            text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}