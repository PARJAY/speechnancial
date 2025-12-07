package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.repository.UserRepositoryImpl
import com.example.speechnancial.newUi.navigation.InputTransactionScreenNav
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// revisi hide dialog agar mengupdate pair code jika tidak kosong
sealed class TransactionScreenEvent {
    data class FilterTransactionByWallet(val walletList: List<Wallet>) : TransactionScreenEvent()
    data class AdjustGraphByTimeRange(val timeRange: EnumTimeRange, val customDateRange: Pair<Timestamp?, Timestamp?>?) : TransactionScreenEvent()
    data class OnTransactionItemClickOpenInputTransactionScreenWithData(val transaction: Transaction, val navHostController: NavHostController) : TransactionScreenEvent()

    data class OnSearchQueryChange(val query: String) : TransactionScreenEvent()
    data class OnFabClickedOpenInputTransactionScreen(val navHostController: NavHostController) : TransactionScreenEvent()
    data object ClearFilters : TransactionScreenEvent()
    data object ShowDialog : TransactionScreenEvent()
    data object HideDialog : TransactionScreenEvent()
    data class SavePairCode(val userId: String, val pairCode: String) : TransactionScreenEvent()
    data class DownloadTransactionReportCSV(val context: Context) : TransactionScreenEvent()
}

data class TransactionScreenUiState(
    val transactions: List<Transaction> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val categories: List<Category> = emptyList(),
    val filteredWallet: List<Wallet> = emptyList(),
    val timeRange: EnumTimeRange? = EnumTimeRange.DAILY,
    val totalBalance: Float = 0f,
    val totalIncome: Float = 0f,
    val totalOutcome: Float = 0f,
    val customDateRange: Pair<Timestamp?, Timestamp?>? = null,
    val selectedTransaction: Transaction? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialog: Boolean = false,
    val searchQuery: String = ""
)

class TransactionUIViewModel(
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val userRepository: UserRepositoryImpl,
    private val currentUserId: String
): ViewModel() {
    private val _uiState = MutableStateFlow(TransactionScreenUiState())
    val uiState: StateFlow<TransactionScreenUiState> = _uiState.asStateFlow()

    private val allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.wallets.collect { wallets : List<Wallet> ->
                _uiState.value = _uiState.value.copy(
                    wallets = wallets,
                    totalBalance = wallets.sumOf { it.balance.toDouble() }.toFloat(),
                    totalIncome = wallets.sumOf { it.totalEarning.toDouble() }.toFloat(),
                    totalOutcome = wallets.sumOf { it.totalSpending.toDouble() }.toFloat()
                )
            }
        }

        viewModelScope.launch {
            firestoreViewModel.categories.collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.transactions.collect { transactions ->
                allTransactions.value = transactions
                filterTransactions(_uiState.value.searchQuery)
            }
        }
    }

    private fun filterTransactions(query: String) {
        val filtered = if (query.isBlank()) {
            allTransactions.value
        } else {
            allTransactions.value.filter {
                it.fullText.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(transactions = filtered) }
    }

    // revisi show dialog jika pair code smartwatch kosong (untuk memastikan user mengisi pair code pertama kali)
    // revisi hide dialog jika pair code sudah terisi bisa di close, jika kosong tidak bisa di close
    fun onEvent(event: TransactionScreenEvent) {
        when (event) {
            is TransactionScreenEvent.FilterTransactionByWallet -> filterTransactionsByWallet(event.walletList)
            is TransactionScreenEvent.AdjustGraphByTimeRange -> adjustGraphByTimeRange(event.timeRange, event.customDateRange)
            is TransactionScreenEvent.OnTransactionItemClickOpenInputTransactionScreenWithData -> onTransactionItemClick(event.transaction, event.navHostController)

            is TransactionScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterTransactions(event.query)
            }

            is TransactionScreenEvent.OnFabClickedOpenInputTransactionScreen -> onFabClicked(event.navHostController)
            TransactionScreenEvent.ClearFilters -> clearFilters()
            TransactionScreenEvent.ShowDialog -> {
                viewModelScope.launch {
                    val user = userRepository.getCustomerById(currentUserId)
                    if (user.pair_code.isEmpty()) {
                        // wajib isi pair code
                        _uiState.update { it.copy(showDialog = true) }
                    } else {
                        // sudah ada → boleh tampil (misal untuk filter transaksi)
                        _uiState.update { it.copy(showDialog = true) }
                    }
                }
            }

            TransactionScreenEvent.HideDialog -> {
                viewModelScope.launch {
                    val user = userRepository.getCustomerById(currentUserId)
                    if (user.pair_code.isNotEmpty()) {
                        _uiState.update { it.copy(showDialog = false) }
                    } else {
                        // kalau kosong → jangan close
                        // opsional: trigger snackbar / errorMessage
                        _uiState.update { it.copy(errorMessage = "Pair code wajib diisi sebelum menutup dialog") }
                    }
                }
            }

            is TransactionScreenEvent.SavePairCode -> {
                viewModelScope.launch {
                    if (event.pairCode.isNotBlank()) {
                        val pairCode = event.pairCode

                        // 🔹 Cek apakah pairCode sudah dipakai user lain
                        userRepository.isPairCodeAvailable(pairCode, event.userId) { available ->
                            if (available) {
                                // Kalau masih available, update
                                viewModelScope.launch {
                                    userRepository.updatePairCode(event.userId, pairCode)
                                    _uiState.update { it.copy(showDialog = false, errorMessage = null) }
                                }
                            } else {
                                // Kalau sudah dipakai orang lain
                                _uiState.update { it.copy(errorMessage = "Pair code sudah digunakan!") }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(errorMessage = "Pair code tidak boleh kosong") }
                    }
                }
            }
            is TransactionScreenEvent.DownloadTransactionReportCSV -> downloadTransactionReportCSV(event.context)
        }
    }

    private fun filterTransactionsByWallet(walletList: List<Wallet>) {
        _uiState.value = _uiState.value.copy(filteredWallet = walletList)
        filterTransactions()
    }

    private fun adjustGraphByTimeRange(
        timeRange: EnumTimeRange,
        customDateRange: Pair<Timestamp?, Timestamp?>?
    ) {
        _uiState.value = _uiState.value.copy(timeRange = timeRange, customDateRange = customDateRange)
        filterTransactions()
    }

    private fun clearFilters() {
        _uiState.value = _uiState.value.copy(filteredWallet = emptyList(), timeRange = EnumTimeRange.DAILY, customDateRange = null)
        filterTransactions()
    }

    private fun filterTransactions() {
        val filteredWallets = _uiState.value.filteredWallet

        viewModelScope.launch {
            firestoreViewModel.transactions.collect { transactions ->
                _uiState.value = _uiState.value.copy(
                    transactions = transactions.filter { transaction ->
                        filteredWallets.isEmpty() || filteredWallets.any { wallet ->
                            transaction.relatedWalletUuid == wallet.uuid
                        }
                    },
                    totalBalance = _uiState.value.filteredWallet.sumOf { it.balance.toDouble() }.toFloat(),
                    totalIncome = _uiState.value.filteredWallet.sumOf { it.totalEarning.toDouble() }.toFloat(),
                    totalOutcome = _uiState.value.filteredWallet.sumOf { it.totalSpending.toDouble() }.toFloat()
                )
            }
        }
    }

    private fun onTransactionItemClick(transaction: Transaction, navHostController: NavHostController) {
        Log.d("TLS", "$transaction")
        firestoreViewModel.setSelectedTransaction(transaction)
        navHostController.navigate(InputTransactionScreenNav)
    }

    private fun onFabClicked(navHostController: NavHostController) {
        navHostController.navigate(InputTransactionScreenNav)
    }

    private fun downloadTransactionReportCSV(context: Context) {
        viewModelScope.launch {
            val transactions = _uiState.value.transactions
            if (transactions.isEmpty()) {
                Toast.makeText(context, "Tidak ada transaksi untuk diunduh.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val csvContent = convertTransactionsToCsv(transactions)
            if (csvContent.isNullOrEmpty()) {
                Toast.makeText(context, "Gagal membuat konten CSV.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val fileName = "transactions_${System.currentTimeMillis()}.csv"
            // Gunakan Environment.DIRECTORY_DOWNLOADS tanpa getExternalFilesDir()
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            try {
                val writer = FileWriter(file)
                writer.append(csvContent)
                writer.flush()
                writer.close()
                Toast.makeText(context, "Laporan transaksi diunduh ke ${file.absolutePath}", Toast.LENGTH_LONG).show()
                Log.d("download file", "Laporan transaksi diunduh ke ${file.absolutePath}")
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan file CSV.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertTransactionsToCsv(transactions: List<Transaction>): String? {
        if (transactions.isEmpty()) return null

        val csvBuilder = StringBuilder()
        csvBuilder.append("UUID,Total,Tanggal,Tipe,Detail\n") // Header

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        transactions.forEach { transaction ->
            csvBuilder.append(transaction.uuid).append(",")
            csvBuilder.append(transaction.total).append(",")
            csvBuilder.append(dateFormat.format(Date(transaction.dateAdded.seconds * 1000))).append(",")
            csvBuilder.append(transaction.transactionTypeOldOrdinalOld).append(",")
            csvBuilder.append(transaction.details?.toString()?.replace(",", ";")).append("\n") // Replace commas in details
        }

        return csvBuilder.toString()
    }
}