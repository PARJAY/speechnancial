package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.repository.BudgetRepository
import com.example.speechnancial.tools.Util.Companion.createOrUpdateBudgetRealizations
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BudgetDetailHistoryEvent {
    data class EditBudget(val budget: Budget, val context: Context) : BudgetDetailHistoryEvent()
    data class DeleteBudget(val budgetUuid: String, val context: Context, val navController: NavHostController) : BudgetDetailHistoryEvent()
    data object ShowDialog : BudgetDetailHistoryEvent()
    data object HideDialog : BudgetDetailHistoryEvent()
}

// ternyata saya harus menambahkan ini val selectedBudgetRealization: BudgetRealization,
data class BudgetDetailHistoryUiState(
    val selectedBudget: Budget,
    val selectedBudgetRealization: BudgetRealization, // Tambahkan selectedBudgetRealization
    val categories: List<Category> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialog: Boolean = false,
    val toastMessage: String? = ""
)

class BudgetDetailHistoryViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val budgetRepository: BudgetRepository,
    budget: Budget,
    budgetRealization: BudgetRealization
) : ViewModel() {
    private val _uiState = MutableStateFlow(BudgetDetailHistoryUiState(
        selectedBudget = budget,
        selectedBudgetRealization = budgetRealization
    ))
    val uiState: StateFlow<BudgetDetailHistoryUiState> = _uiState.asStateFlow()

    init {
        logDataFlowToFile("BudgetDetailHistoryViewModel", "")
        logDataFlowToFile("BudgetDetailHistoryViewModel", "")
        logDataFlowToFile("BudgetDetailHistoryViewModel", "")
        logDataFlowToFile("BudgetDetailHistoryViewModel", "Entering BudgetDetailHistory Screen")
        logDataFlowToFile("BudgetDetailHistoryViewModel", "")

//        Log.d("BudgetDetailHistoryViewModel", "run?")
        viewModelScope.launch {
//            Log.d("BudgetDetailHistoryViewModel", "collect categories?")
            firestoreViewModel.categories.collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.budgets.collect { budgets ->
//                Log.d("BudgetDetailHistoryViewModel", "collect budget?")
                val currentBudget = budgets.find { it.uuid == budget.uuid } ?: budget
                val sortedRealizations = currentBudget.budgetRealizations.sortedByDescending { it.startTime }

                // Cari BudgetRealization yang sesuai
                val selectedRealization = sortedRealizations.find { it.uuid == budgetRealization.uuid } ?: budgetRealization

                _uiState.value = _uiState.value.copy(
                    selectedBudget = currentBudget.copy(budgetRealizations = sortedRealizations),
                    selectedBudgetRealization = selectedRealization
                )
            }
        }
//        Log.d("BudgetDetailHistoryViewModel", "collect related transactions?")
        viewModelScope.launch {
            // Ambil semua data transaksi dengan id yang sama dengan budget.BudgetRealization.involvedTransactions
            val transactionIds = budget.budgetRealizations.flatMap { it.involvedTransactionsUuid ?: emptyList() }
//            Log.d("BudgetDetailHistoryViewModel", "$transactionIds")
            if (transactionIds.isNotEmpty()) {
                firestoreViewModel.transactions.collect { transactions ->
//                    Log.d("BudgetDetailHistoryViewModel", "$transactions")
                    val filteredTransactions = transactions.filter { it.uuid in transactionIds }
                    _uiState.value = _uiState.value.copy(transactions = filteredTransactions)
                }
            }
        }
    }

    fun onEvent(event: BudgetDetailHistoryEvent) {
        when (event) {
            is BudgetDetailHistoryEvent.EditBudget -> editBudget(event.budget, event.context)
            is BudgetDetailHistoryEvent.DeleteBudget -> deleteBudgetAndCloseScreen(event.budgetUuid, event.context, event.navController)
            BudgetDetailHistoryEvent.ShowDialog -> _uiState.value = _uiState.value.copy(showDialog = true)
            BudgetDetailHistoryEvent.HideDialog -> _uiState.value = _uiState.value.copy(showDialog = false)
        }
    }

    private fun editBudget(budget: Budget, context: Context) {
        viewModelScope.launch {
            val updatedBudget = budget.copy(budgetRealizations = createOrUpdateBudgetRealizations(budget, _uiState.value.transactions))
            budgetRepository.updateBudget(userId, updatedBudget) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                logDataFlowToFile("BudgetDetailHistoryViewModel", "Budget UPDATED: ${budget.uuid}")
                logDataFlowToFile("BudgetDetailHistoryViewModel", "updated Budget:   $updatedBudget")
                logDataFlowToFile("BudgetDetailHistoryViewModel", "")
            }

            onEvent(BudgetDetailHistoryEvent.HideDialog)
        }
    }

    private fun deleteBudgetAndCloseScreen(budgetUuid: String, context: Context, navController: NavHostController) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(userId, budgetUuid) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                logDataFlowToFile("BudgetDetailHistoryViewModel", "Budget DELETED: $budgetUuid")
                logDataFlowToFile("BudgetDetailHistoryViewModel", "")
            }

            navController.popBackStack()
            navController.popBackStack()
        }
    }
}