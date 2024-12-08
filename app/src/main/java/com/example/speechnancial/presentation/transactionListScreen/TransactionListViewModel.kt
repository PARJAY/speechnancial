package com.example.speechnancial.presentation.transactionListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class TransactionListViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    private val _transaction = transactionDao.getAllTransactions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TransactionListState())

    val state = combine(_state, _transaction) { state, transaction ->
        state.copy(
            transactionList = transaction
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionListState())

    // onevent
    fun onEvent(event : TransactionListEvent) {
        when(event) {
            is TransactionListEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    transactionDao.deleteTransaction(event.transaction)
                }
            }
            TransactionListEvent.UpdateSelectedTransaction -> {
                viewModelScope.launch {
                    // todo : selain ngerubah rawTextnya, aku juga harus ngerubah transaction detailsnya
                    transactionDao.updateTransaction(state.value.selectedTransaction)
                }
            }

            is TransactionListEvent.ShowDialog -> {
                _state.update { it.copy(
                    selectedTransaction = event.transaction,
                    showUpdateTransactionDialog = true
                ) }
            }

            is TransactionListEvent.HandleUserInput -> {
                _state.update {
                    it.copy(
                        selectedTransaction = it.selectedTransaction.copy(rawText = event.userInput)
                    )
                }
            }

            TransactionListEvent.HideDialog -> {
                _state.update { it.copy(
                    selectedTransaction = Transaction(),
                    showUpdateTransactionDialog = false
                ) }
            }
        }
    }
}