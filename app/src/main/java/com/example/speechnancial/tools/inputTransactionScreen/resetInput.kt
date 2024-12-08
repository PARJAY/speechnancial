package com.example.speechnancial.tools.inputTransactionScreen

import com.example.speechnancial.presentation.inputTransactionScreen.InputTransactionState

fun resetInput(state: InputTransactionState) {
    state.source.value = ""
    state.previousPartialResult.value = ""
}