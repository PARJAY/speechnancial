package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.data.firebase.model.Keyword
import com.example.speechnancial.data.firebase.repository.CategoryRepository
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class CategoryScreenEvent {
    data class IncomeFilterChanged(val enabled: Boolean) : CategoryScreenEvent()
    data class OutcomeFilterChanged(val enabled: Boolean) : CategoryScreenEvent()
    data class OnCategoryItemClickOpenCategoryDetailScreenWithData(val category: Category) : CategoryScreenEvent()

    data class OnSearchQueryChange(val query: String) : CategoryScreenEvent()

    data object OpenDialog : CategoryScreenEvent()
    data object CloseDialog : CategoryScreenEvent()
    data class SetSelectedCategory(val category: Category) : CategoryScreenEvent()
    data object ResetSelectedCategory : CategoryScreenEvent()

    data class SaveCategory(val category: Category, val context: Context) : CategoryScreenEvent()
    data class UpdateCategory(val updatedCategory: Category, val context: Context) : CategoryScreenEvent()
    data class SoftDeleteCategory(val categoryUuid: String, val context: Context) : CategoryScreenEvent()
    data class HardDeleteCategory(val categoryUuid: String, val context: Context) : CategoryScreenEvent()

    data class ShowToast(val message: String) : CategoryScreenEvent()
}

data class CategoryScreenUiState(
    val categories: List<Category> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val keywords: List<Keyword> = emptyList(),

    val isIncomeFilterEnabled: Boolean = false,
    val isOutcomeFilterEnabled: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val showDialogCategoryCrud: Boolean = false,
    val selectedCategory: Category = Category(enumTransactionType = EnumTransactionType.OUTCOME.ordinal),

    val toastMessage: String? = null,
    val expandedTransactionTypeDropdown: Boolean = false,
    val searchQuery: String = ""
)

class CategoryUIViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val repository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryScreenUiState())
    val uiState: StateFlow<CategoryScreenUiState> = _uiState.asStateFlow()

    private val allCategories = MutableStateFlow<List<Category>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.categories.collect { category ->
                allCategories.value = category
                filterCategories(_uiState.value.searchQuery)
            }
        }
    }

    private fun filterCategories(query: String) {
        val filtered = if (query.isBlank()) {
            allCategories.value
        } else {
            allCategories.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(categories = filtered) }
    }

    fun onEvent(event: CategoryScreenEvent) {
        when (event) {
            is CategoryScreenEvent.OnCategoryItemClickOpenCategoryDetailScreenWithData -> {
                _uiState.update { it.copy(showDialogCategoryCrud = true, selectedCategory = event.category) }
            }

            is CategoryScreenEvent.IncomeFilterChanged -> {
                _uiState.update { it.copy(isIncomeFilterEnabled = event.enabled) }
                fetchData(event.enabled, uiState.value.isOutcomeFilterEnabled, firestoreViewModel.categories.value)
            }
            is CategoryScreenEvent.OutcomeFilterChanged -> {
                _uiState.update { it.copy(isOutcomeFilterEnabled = event.enabled) }
                fetchData(uiState.value.isIncomeFilterEnabled, event.enabled, firestoreViewModel.categories.value)
            }

            is CategoryScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterCategories(event.query)
            }

            is CategoryScreenEvent.SaveCategory -> { saveCategory(event.category, event.context) }
            is CategoryScreenEvent.UpdateCategory -> { updateCategory(event.updatedCategory, event.context) }
            is CategoryScreenEvent.HardDeleteCategory -> { deleteCategory(event.categoryUuid, event.context) }
            is CategoryScreenEvent.SoftDeleteCategory -> { softDeleteCategory(event.categoryUuid, event.context) }

            CategoryScreenEvent.OpenDialog -> _uiState.update { it.copy(showDialogCategoryCrud = true) }
            CategoryScreenEvent.CloseDialog -> {
                onEvent(CategoryScreenEvent.ResetSelectedCategory)
                _uiState.update { it.copy(showDialogCategoryCrud = false) }
            }

            is CategoryScreenEvent.SetSelectedCategory -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }


            is CategoryScreenEvent.ResetSelectedCategory -> {
                _uiState.update { it.copy(selectedCategory = Category(enumTransactionType = EnumTransactionType.OUTCOME.ordinal)) }
            }

            is CategoryScreenEvent.ShowToast -> {
                _uiState.update { it.copy(toastMessage = event.message) }
            }
        }
    }

    private fun fetchData(isIncomeFilterEnabled: Boolean, isOutcomeFilterEnabled: Boolean, categories: List<Category>) {
        val filteredCategories = categories.filter { category ->
            (isIncomeFilterEnabled && EnumTransactionType.INCOME.ordinal == category.enumTransactionType) ||
            (isOutcomeFilterEnabled && EnumTransactionType.OUTCOME.ordinal == category.enumTransactionType) ||
            (!isIncomeFilterEnabled && !isOutcomeFilterEnabled)
        }
        _uiState.update { it.copy(categories = filteredCategories) }
    }

    private fun saveCategory(category: Category, context: Context) {
        viewModelScope.launch {
            repository.addCategory(
                userId,
                category,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    logDataFlowToFile("CategoryVM", "Category ADDED: ${category.uuid}")
                    logDataFlowToFile("CategoryVM", "full Category: $category")
                    logDataFlowToFile("CategoryVM", "")
                }
            )
        }
        onEvent(CategoryScreenEvent.ResetSelectedCategory)
        onEvent(CategoryScreenEvent.CloseDialog)
    }

    private fun updateCategory(category: Category, context: Context) {
        val old = uiState.value.categories.find { it.uuid == category.uuid }

        viewModelScope.launch {
            repository.updateCategory(
                userId,
                category,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    logDataFlowToFile("CategoryVM", "Category UPDATED: ${category.uuid}")
                    logDataFlowToFile("CategoryVM", "from: $old")
                    logDataFlowToFile("CategoryVM", "to:   $category")
                    logDataFlowToFile("CategoryVM", "")
                }
            )
        }
        onEvent(CategoryScreenEvent.ResetSelectedCategory)
        onEvent(CategoryScreenEvent.CloseDialog)
    }

    private fun deleteCategory(categoryUuid: String, context: Context) {
        viewModelScope.launch {
            repository.deleteCategory(
                userId,
                categoryUuid,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                    logDataFlowToFile("CategoryVM", "Category DELETED: $categoryUuid")
                    logDataFlowToFile("CategoryVM", "")
                }
            )
        }
        onEvent(CategoryScreenEvent.ResetSelectedCategory)
        onEvent(CategoryScreenEvent.CloseDialog)
    }

    private fun softDeleteCategory(categoryUuid: String, context: Context) {
        viewModelScope.launch {
            repository.softDeleteCategory(
                userId,
                categoryUuid,
                callback = { messange ->
                    Toast.makeText(context, messange, Toast.LENGTH_SHORT).show()
                }
            )
        }
        onEvent(CategoryScreenEvent.ResetSelectedCategory)
        onEvent(CategoryScreenEvent.CloseDialog)
    }
}