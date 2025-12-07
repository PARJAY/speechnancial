package com.example.speechnancial.viewmodel.transactionListScreen.firebase

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.MyApp
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.data.model.WalletBalanceAndHistory
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListEvent
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TransactionFirebaseViewModel (
    dataStoreManager: WalletDataStoreManager
) : ViewModel() {
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

    val state: StateFlow<TransactionListState> = combine(_state, _walletData) { state, walletData ->
        state.copy(
            walletBalanceAndHistory = walletData
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransactionListState())

    init {
        MyApp.appModule.transactionRepositoryImpl.getTransactionList(
            "dummy Id",
            errorCallback = {
                Log.d("HotelScreen", "error : $it")
            },
            addDataCallback = { transaction ->
//                _state.update { it.copy(
//                        transactionList = it.transactionList + transaction
//                ) }
                Log.d("TransactionListFirebaseVM", "added transaction : $transaction")
            },
            updateDataCallback = { updatedData ->
//                _state.update { currentState ->
//                    val updatedList = currentState.transactionList.map { transaction ->
//                        if (transaction.id == updatedData.id) { // Asumsi 'id' adalah properti unik
//                            updatedData // Ganti transaction lama dengan data yang baru
//                        } else {
//                            transaction // Biarkan transaction lain tidak berubah
//                        }
//                    }
//                    currentState.copy(transactionList = updatedList)
//                }
                Log.d("TransactionListFirebaseVM", "updated transaction : $updatedData")
            },
            deleteDataCallback = { documentId: String ->
                _state.update { currentState ->
                    val updatedList = currentState.transactionList.filter { transaction ->
                        transaction.id != documentId // Filter berdasarkan id
                    }
                    currentState.copy(transactionList = updatedList)
                }
                Log.d("TransactionListFirebaseVM", "deleted transaction with id: $documentId")
            }
        )

        // old firebase method
/*        val transactionCollection = firestore.collection("transaction-list")
//        transactionCollection.addSnapshotListener { value, error ->
//            if (error != null) {
//                Log.w("TAG", "Listen failed.", error)
//                return@addSnapshotListener
//            }
//
//            value?.let { querySnapshot ->
//
//                querySnapshot.forEach { queryDocumentSnapshot ->
//                    _state.update {
//                        it.copy(
//                            transactionList = it.transactionList + Transaction(
//                                id = queryDocumentSnapshot.id,
//                                rawText = queryDocumentSnapshot.getString("rawText") ?: "",
////                                type = ,
////                                details = ,
//                                total = queryDocumentSnapshot.getLong("total")?.toFloat() ?: 0f,
//                                createdAt = queryDocumentSnapshot.getTimestamp("createdAt"),
//                                isReviseNeeded = queryDocumentSnapshot.getBoolean("isReviseNeeded") ?: false,
//                                isTranscriptionError = queryDocumentSnapshot.getBoolean("isTranscriptionError") ?: false,
//                                isValid = queryDocumentSnapshot.getBoolean("isValid") ?: false,
//                                isFromSmartwatch = queryDocumentSnapshot.getBoolean("isFromSmartwatch") ?: false,
//                            )
//                        )
//                    }
//                }
//            }
//        }
 */
    }

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