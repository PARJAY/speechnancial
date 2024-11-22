package com.example.speechnancial.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomCheckbox(isChecked: Boolean, text: String, isEnabled: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
            enabled = isEnabled
        )
        Text(text)
    }
}