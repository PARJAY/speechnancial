package com.example.speechnancial.data.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Keyword
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BudgetDetailScreenEvent {
    data class OnBudgetNameChanged(val name: String) : BudgetDetailScreenEvent()
    data class OnBudgetBalanceChanged(val balance: Float) : BudgetDetailScreenEvent()
    data class OnBudgetStartTimeChanged(val startTime: Timestamp) : BudgetDetailScreenEvent()
    data class OnBudgetRecurringTypeChanged(val recurringTypeOrdinal: Int) : BudgetDetailScreenEvent()
    data class OnBudgetTimeRangeChanged(val timeRange: Int) : BudgetDetailScreenEvent()
    data class OnBudgetCategoryAdded(val category: String) : BudgetDetailScreenEvent()
    data class OnBudgetCategoryRemoved(val category: String) : BudgetDetailScreenEvent()
    data class OnPerformSaveBudgetWhenItNotEmpty(val budget: Budget) : BudgetDetailScreenEvent()
    data class OnPerformUpdateBudgetWhenItChangedAndNotEmpty(val budget: Budget) : BudgetDetailScreenEvent()
    data class OnSoftDeleteButtonClicked(val budgetUuid: String) : BudgetDetailScreenEvent()
}

data class BudgetDetailScreenUiState(
    val budget: Budget? = null,
    val categories: List<Category> = emptyList(),
    val keywords: List<Keyword> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false
)

class BudgetDetailScreenUIViewModel(
    private val firestoreViewModel: FirestoreCollectionViewModel,
    passedBudget: Budget? = null // Added passedBudget parameter
) : ViewModel() {
    private val _uiState = MutableStateFlow(BudgetDetailScreenUiState(budget = passedBudget)) // Initialize with passedBudget
    val uiState: StateFlow<BudgetDetailScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firestoreViewModel.categories.collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories)
            }
        }

        viewModelScope.launch {
            firestoreViewModel.keywords.collect { keywords ->
                _uiState.value = _uiState.value.copy(keywords = keywords)
            }
        }
    }

    fun onEvent(event: BudgetDetailScreenEvent) {
        when (event) {
            is BudgetDetailScreenEvent.OnBudgetNameChanged -> onBudgetNameChanged(event.name)
            is BudgetDetailScreenEvent.OnBudgetBalanceChanged -> onBudgetBalanceChanged(event.balance)
            is BudgetDetailScreenEvent.OnBudgetStartTimeChanged -> onBudgetStartTimeChanged(event.startTime)
            is BudgetDetailScreenEvent.OnBudgetRecurringTypeChanged -> onBudgetRecurringTypeChanged(event.recurringTypeOrdinal)
            is BudgetDetailScreenEvent.OnBudgetTimeRangeChanged -> onBudgetTimeRangeChanged(event.timeRange)
            is BudgetDetailScreenEvent.OnBudgetCategoryAdded -> onBudgetCategoryAdded(event.category)
            is BudgetDetailScreenEvent.OnBudgetCategoryRemoved -> onBudgetCategoryRemoved(event.category)
            is BudgetDetailScreenEvent.OnPerformSaveBudgetWhenItNotEmpty -> onPerformSaveBudgetWhenItNotEmpty(event.budget)
            is BudgetDetailScreenEvent.OnPerformUpdateBudgetWhenItChangedAndNotEmpty -> onPerformUpdateBudgetWhenItChangedAndNotEmpty(event.budget)
            is BudgetDetailScreenEvent.OnSoftDeleteButtonClicked -> onSoftDeleteButtonClicked(event.budgetUuid)
        }
    }

    private fun onBudgetNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(name = name))
    }

    private fun onBudgetBalanceChanged(balance: Float) {
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(amount = balance))
    }

    private fun onBudgetStartTimeChanged(startTime: Timestamp) {
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(startTime = startTime))
    }

    private fun onBudgetRecurringTypeChanged(recurringTypeOrdinal: Int) {
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(recurringTypeOrdinal = recurringTypeOrdinal))
    }

    private fun onBudgetTimeRangeChanged(timeRange: Int) {
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(timeRangeInDays = timeRange))
    }

    private fun onBudgetCategoryAdded(category: String) {
        // Implement logic to add category to budget
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(involvedCategoriesUuid = _uiState.value.budget?.involvedCategoriesUuid?.plus(category) ?: listOf(category)))
    }

    private fun onBudgetCategoryRemoved(category: String) {
        // Implement logic to remove category from budget
        _uiState.value = _uiState.value.copy(isEditing = true, budget = _uiState.value.budget?.copy(involvedCategoriesUuid = _uiState.value.budget?.involvedCategoriesUuid?.filter { it != category }))
    }

    private fun onPerformSaveBudgetWhenItNotEmpty(budget: Budget) {
        // Implement save budget logic
    }

    private fun onPerformUpdateBudgetWhenItChangedAndNotEmpty(budget: Budget) {
        // Implement update budget logic
    }

    private fun onSoftDeleteButtonClicked(budgetUuid: String) {
        // Implement soft delete budget logic
    }
}