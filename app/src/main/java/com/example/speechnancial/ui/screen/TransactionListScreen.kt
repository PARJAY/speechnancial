package com.example.speechnancial.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun TransactionListScreen() {
    Text("TransactionListScreen")
}


@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    SpeechnancialTheme {
        TransactionListScreen()
    }
}