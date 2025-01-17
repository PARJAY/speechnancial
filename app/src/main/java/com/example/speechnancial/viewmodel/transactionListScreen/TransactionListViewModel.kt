package com.example.speechnancial.viewmodel.transactionListScreen

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.dao.TransactionDao
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.data.model.WalletBalanceAndHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TransactionListViewModel(
    transactionDao: TransactionDao,
    dataStoreManager: WalletDataStoreManager
) : ViewModel() {
    private val _transaction = transactionDao.getAllSortedTransactions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TransactionListState())

    private val _walletData = combine(
        dataStoreManager.getWalletBalance(),
        dataStoreManager.getTotalSpending(),
        dataStoreManager.getTotalEarning()
    ) { balance, totalSpending, totalEarnings ->
        WalletBalanceAndHistory(balance, totalSpending, totalEarnings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WalletBalanceAndHistory()
    )

    val state: StateFlow<TransactionListState> = combine(_state, _transaction, _walletData) { state, transaction, walletData ->
        state.copy(
            transactionList = transaction,
            walletBalanceAndHistory = walletData
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionListState())

    fun onEvent(event : TransactionListEvent) {
        when(event) {
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

            is TransactionListEvent.SettingButtonClick -> {
                Toast.makeText(event.context, "not yet implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }
}