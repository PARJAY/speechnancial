package com.example.speechnancial.presentation.inputTransactionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.model.Transaction
import kotlinx.coroutines.launch

class InputTransactionViewModel(
    private val transactionDao: TransactionDao,
    // p : InputTransactionState        // Good idea, but not for now
) : ViewModel() {
    private fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionDao.insertTransaction(transaction)
    }

    fun onEvent(event : InputTransactionEvent) {
        when(event) {
            is InputTransactionEvent.SaveTransaction -> {
                viewModelScope.launch {
                    insertTransaction(event.transaction)
                }
            }
        }
    }
}