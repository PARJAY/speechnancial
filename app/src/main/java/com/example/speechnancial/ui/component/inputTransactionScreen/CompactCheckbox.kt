package com.example.speechnancial.ui.component.inputTransactionScreen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun CompactCheckbox(
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    text: String
) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = {
            onCheckedChange()
        },
        colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.onTertiary,
            checkmarkColor = Color.White
        ),

        //below line is uses an interaction source
        // that handles interaction events for the checkbox
        interactionSource = remember { MutableInteractionSource() }
    )
    Text(text)
}