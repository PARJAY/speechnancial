package com.example.speechnancial.viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.data.db.AppDatabase
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionViewModel
import com.example.speechnancial.viewmodel.inputTransactionScreen.firebase.InputTransactionFirebaseViewModel
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListViewModel
import com.example.speechnancial.viewmodel.transactionListScreen.firebase.TransactionFirebaseViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun makeInputTransactionVM(db : AppDatabase, context: Context, dataStoreManager: WalletDataStoreManager) : InputTransactionViewModel {
    return viewModel(
        factory = viewModelFactory {
            InputTransactionViewModel(
                db.transactionDao(),
                context,
                dataStoreManager
            )
        }
    )
}

@Composable
fun makeInputTransactionFirebaseVM(firestore: FirebaseFirestore, context: Context, dataStoreManager: WalletDataStoreManager) : InputTransactionFirebaseViewModel {
    return viewModel(
        factory = viewModelFactory {
            InputTransactionFirebaseViewModel(
                firestore,
                context,
                dataStoreManager
            )
        }
    )
}

@Composable
fun makeTransactionListVM(db : AppDatabase, dataStoreManager: WalletDataStoreManager) : TransactionListViewModel {
    return viewModel(
        factory = viewModelFactory {
            TransactionListViewModel(
                db.transactionDao(),
                dataStoreManager
            )
        }
    )
}

@Composable
fun makeTransactionListFirebaseVM(dataStoreManager: WalletDataStoreManager) : TransactionFirebaseViewModel {
    return viewModel(
        factory = viewModelFactory {
            TransactionFirebaseViewModel(
                dataStoreManager
            )
        }
    )
}