package com.example.speechnancial.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.data.db.AppDatabase
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionViewModel
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListViewModel

@Composable
fun makeInputTransactionVM(db : AppDatabase) : InputTransactionViewModel {
    return viewModel(
        factory = viewModelFactory {
            InputTransactionViewModel(
                db.transactionDao()
            )
        }
    )
}

@Composable
fun makeTransactionListVM(db : AppDatabase) : TransactionListViewModel {
    return viewModel(
        factory = viewModelFactory {
            TransactionListViewModel(
                db.transactionDao()
            )
        }
    )
}