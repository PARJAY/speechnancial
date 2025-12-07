package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.repository.BudgetRepository
import com.example.speechnancial.tools.Util.Companion.createOrUpdateBudgetRealizations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class BudgetScreenEvent {
    data class SaveBudget(val budget: Budget, val context: Context) : BudgetScreenEvent()

    data class OnSearchQueryChange(val query: String) : BudgetScreenEvent()

    data object OpenDialog : BudgetScreenEvent()
    data object CloseDialog : BudgetScreenEvent()
    data class ShowToast(val message: String) : BudgetScreenEvent()
}

data class BudgetScreenUiState(
    val budgets: List<Budget> = emptyList(),
    val categories: List<Category> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialogBudgetCrud: Boolean = false,
    val selectedBudget: Budget? = null,
    val toastMessage: String? = null,
    val searchQuery: String = "",
)

class BudgetListScreenUIViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val budgetRepository: BudgetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BudgetScreenUiState())
    val uiState: StateFlow<BudgetScreenUiState> = _uiState.asStateFlow()

    private val allWallets = MutableStateFlow<List<Budget>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.budgets.collect { budgets ->
                allWallets.value = budgets
                filterBudgets(_uiState.value.searchQuery)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.categories.collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.transactions.collect{
                transactions -> _uiState.value = _uiState.value.copy(transactions = transactions)
            }
        }
    }

    private fun filterBudgets(query: String) {
        val filtered = if (query.isBlank()) {
            allWallets.value
        } else {
            allWallets.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(budgets = filtered) }
    }

    fun onEvent(event: BudgetScreenEvent) {
        when (event) {
            is BudgetScreenEvent.SaveBudget -> saveBudget(event.budget, event.context)

            is BudgetScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterBudgets(event.query)
            }

            BudgetScreenEvent.OpenDialog -> openDialog()
            BudgetScreenEvent.CloseDialog -> closeDialog()
            is BudgetScreenEvent.ShowToast -> showToast(event.message)
        }
    }

    private fun saveBudget(budget: Budget, context: Context) {
        viewModelScope.launch {
            val savedBudget = budget.copy(
                budgetRealizations = createOrUpdateBudgetRealizations(budget, _uiState.value.transactions)
            )

            budgetRepository.addBudget(userId, savedBudget) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        closeDialog()
    }

    private fun openDialog() {
        _uiState.value = _uiState.value.copy(showDialogBudgetCrud = true)
    }

    private fun closeDialog() {
        _uiState.value = _uiState.value.copy(showDialogBudgetCrud = false)
    }

    private fun showToast(message: String) {
        _uiState.value = _uiState.value.copy(toastMessage = message)
    }
}