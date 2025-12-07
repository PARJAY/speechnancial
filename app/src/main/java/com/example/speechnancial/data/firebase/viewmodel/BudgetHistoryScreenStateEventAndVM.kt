package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.repository.BudgetRepository
import com.example.speechnancial.tools.Util.Companion.createOrUpdateBudgetRealizations
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BudgetHistoryEvent {
    data class EditBudget(val budget: Budget, val context: Context) : BudgetHistoryEvent()
    data class DeleteBudget(val budgetUuid: String, val context: Context, val navController: NavHostController) : BudgetHistoryEvent()
    data object ShowDialog : BudgetHistoryEvent()
    data object HideDialog : BudgetHistoryEvent()
}

data class BudgetHistoryUiState(
    val selectedBudget: Budget,
    val categories: List<Category> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialog: Boolean = false,
    val toastMessage: String? = ""
)

class BudgetHistoryViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val budgetRepository: BudgetRepository,
    budget: Budget
) : ViewModel() {
    private val _uiState = MutableStateFlow(BudgetHistoryUiState(selectedBudget = budget))
    val uiState: StateFlow<BudgetHistoryUiState> = _uiState.asStateFlow()

    init {
        logDataFlowToFile("BudgetDetailVM", "")
        logDataFlowToFile("BudgetDetailVM", "")
        logDataFlowToFile("BudgetDetailVM", "")
        logDataFlowToFile("BudgetDetailVM", "Entering BudgetHistory Screen")
        logDataFlowToFile("BudgetDetailVM", "")

        viewModelScope.launch {
            firestoreViewModel.categories.collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.transactions.collect { transactions ->
                _uiState.value = _uiState.value.copy(transactions = transactions)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.budgets.collect { budgets ->
                val currentBudget = budgets.find { it.uuid == budget.uuid } ?: budget
                val sortedRealizations = currentBudget.budgetRealizations.sortedByDescending { it.startTime }
                _uiState.value = _uiState.value.copy(
                    selectedBudget = currentBudget.copy(budgetRealizations = sortedRealizations)
                )
            }
        }
    }

    fun onEvent(event: BudgetHistoryEvent) {
        when (event) {
            is BudgetHistoryEvent.EditBudget -> editBudget(event.budget, event.context)
            is BudgetHistoryEvent.DeleteBudget -> deleteBudgetAndCloseScreen(event.budgetUuid, event.context, event.navController)
            BudgetHistoryEvent.ShowDialog -> _uiState.value = _uiState.value.copy(showDialog = true)
            BudgetHistoryEvent.HideDialog -> _uiState.value = _uiState.value.copy(showDialog = false)
        }
    }

    private fun editBudget(budget: Budget, context: Context) {
        viewModelScope.launch {
            val updatedBudget = budget.copy(budgetRealizations = createOrUpdateBudgetRealizations(budget, _uiState.value.transactions))
            budgetRepository.updateBudget(userId, updatedBudget) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                logDataFlowToFile("BudgetDetailVM", "Budget UPDATED: ${budget.uuid}")
                logDataFlowToFile("BudgetDetailVM", "updated Budget:   $updatedBudget")
                logDataFlowToFile("BudgetDetailVM", "")
            }

            onEvent(BudgetHistoryEvent.HideDialog)
        }
    }

    private fun deleteBudgetAndCloseScreen(budgetUuid: String, context: Context, navController: NavHostController) {
        viewModelScope.launch {
            budgetRepository.deleteBudget(userId, budgetUuid) { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                logDataFlowToFile("BudgetDetailVM", "Budget DELETED: $budgetUuid")
                logDataFlowToFile("BudgetDetailVM", "")
            }

            navController.popBackStack()
            navController.popBackStack()
        }
    }
}