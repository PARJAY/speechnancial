package com.example.speechnancial.data.firebase.viewmodel.failed
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.speechnancial.data.firebase.repository.TransactionRepository
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.launch
//
//// todo : belum sempurna, dan belum di uji
//// todo : variable state belum ada
//// todo : mending pake mutable state flow
//// todo : tiap init belum  ada aksi untuk CRUD pada variable state
//
//class TransactionFirebaseViewModel(db: FirebaseFirestore) : ViewModel() {
//    private val transactionRepository = TransactionRepository(db)
//
//    // LiveData untuk menyimpan data transaksi
//    // Gunakan MutableLiveData jika Anda ingin data dapat diubah di dalam ViewModel
////     private val _transactions = MutableLiveData<List<Transaction>>()
////     val transactions: LiveData<List<Transaction>> = _transactions
//
//    init {
//        getTransactionData()
//    }
//
//    private fun getTransactionData() {
//        viewModelScope.launch { // Gunakan viewModelScope untuk coroutine
//            transactionRepository.getTransactionList(
//                errorCallback = { exception ->
//                    // Handle error, misalnya dengan menampilkan pesan error
//                    println("Error getting transactions: ${exception.message}")
//                },
//                addDataCallback = { transaction ->
//                    // Tambahkan data transaksi baru ke LiveData
//                    // _transactions.value = (_transactions.value ?: emptyList()) + listOf(transaction)
//                    println("Transaction added: ${transaction.uuid}")
//                },
//                updateDataCallback = { transaction ->
//                    // Update data transaksi yang ada di LiveData
//                    // _transactions.value = _transactions.value?.map {
//                    //     if (it.uuid == transaction.uuid) transaction else it
//                    // }
//                    println("Transaction updated: ${transaction.uuid}")
//                },
//                deleteDataCallback = { documentId ->
//                    // Hapus data transaksi dari LiveData
//                    // _transactions.value = _transactions.value?.filter { it.uuid != documentId }
//                    println("Transaction deleted: $documentId")
//                }
//            )
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        transactionRepository.removeListener() // Hapus listener saat ViewModel di-clear
//    }
//}