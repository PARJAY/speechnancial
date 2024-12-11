package com.example.speechnancial.viewmodel.transactionListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.tools.createTransactionFromInput
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
            transactionList = transaction.map { TransactionItemState(it, false) }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionListState())

    fun onEvent(event : TransactionListEvent) {
        when(event) {
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

            TransactionListEvent.DialogActionDeleteTransaction -> {
                viewModelScope.launch { transactionDao.deleteTransaction(state.value.selectedTransaction) }
            }

            TransactionListEvent.DialogActionUpdateSelectedTransaction -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedTransaction =
                            createTransactionFromInput(state.value.selectedTransaction.rawText)
                        )
                    }
                    transactionDao.updateTransaction(state.value.selectedTransaction)
                }
            }

            TransactionListEvent.HideDialog -> {
                _state.update { it.copy(
                    selectedTransaction = Transaction(),
                    showUpdateTransactionDialog = false
                ) }
            }

            TransactionListEvent.FilterEarningButtonClick -> {
                _state.update { it.copy(
                    isFilterEarningActive = !state.value.isFilterEarningActive
                ) }
            }

            TransactionListEvent.FilterSpendingButtonClick -> {
                _state.update { it.copy(
                    isFilterSpendingActive = !state.value.isFilterSpendingActive
                ) }
            }

            TransactionListEvent.SettingButtonClick -> {
                TODO("showToast")
            }
        }
    }
}