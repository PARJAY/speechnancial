package com.example.speechnancial.presentation.inputTransactionScreen

import com.example.speechnancial.data.model.Transaction

sealed interface InputTransactionEvent {
    data class SaveTransaction(val transaction : Transaction): InputTransactionEvent
}