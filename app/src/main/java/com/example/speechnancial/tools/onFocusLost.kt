package com.example.speechnancial.tools
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.onFocusLost(onFocusLost: () -> Unit): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var focusLostCalled by remember { mutableStateOf(false) }

    Modifier.onFocusChanged { focusState ->
        if (isFocused && !focusState.isFocused) {
            // Focus lost
            if (!focusLostCalled) { // Check if the function has already been called
                onFocusLost()
                focusLostCalled = true // Set the flag to prevent further calls
            }
        } else if (focusState.isFocused) {
            // Focus gained
            isFocused = true
            focusLostCalled = false // Reset the flag when focus is gained again
        }
    }
}