package com.example.speechnancial.ui.component

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

// source : https://stackoverflow.com/questions/75189274/how-can-i-run-validation-when-my-users-exit-textfield-in-compose

@Composable
fun OnFocusChangeConvertTextToTransactionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onUnFocused: () -> Unit = {}
) {
    var isFirstFocus by remember { mutableStateOf(true) }
    TextField(value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged { focusState ->
                if (isFirstFocus) {
                    isFirstFocus = false
                } else {
                    if (!focusState.isFocused)
                        onUnFocused()
                }
            })
}