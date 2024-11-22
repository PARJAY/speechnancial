package com.example.speechnancial.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun TransactionItemDev(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TransactionItemDevPreview() {
    SpeechnancialTheme {
        TransactionItemDev("Android")
    }
}