package com.example.speechnancial.data.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.Keyword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CategoryDetailScreenEvent {
    data class OnCategoryNameChanged(val name: String) : CategoryDetailScreenEvent()
    data class DuplicateKeywordChecker(val keyword: String) : CategoryDetailScreenEvent()
    data class OnKeywordAdded(val keyword: String) : CategoryDetailScreenEvent()
    data class OnKeywordRemoved(val keyword: String) : CategoryDetailScreenEvent()
    data class OnCategoryTransactionTypeChangedShowDropdownDialogWithTransactionTypeEnumChoice(val transactionTypeOrdinal: Int) : CategoryDetailScreenEvent()
    data class OnCategoryColorChanged(val selectedColor: String) : CategoryDetailScreenEvent()
    data class OnPerformSaveCategoryWhenItNotEmpty(val category: Category) : CategoryDetailScreenEvent()
    data class OnPerformUpdateCategoryWhenItChangedAndNotEmpty(val category: Category) : CategoryDetailScreenEvent()
    data class OnSoftDeleteButtonClicked(val categoryUuid: String) : CategoryDetailScreenEvent()
}

data class CategoryDetailScreenUiState(
    val category: Category = Category(),
    val keywords: List<Keyword> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false // Example: Added isEditing property
)

class CategoryDetailUIViewModel(
    private val firestoreViewModel: FirestoreCollectionViewModel,
    passedCategory: Category = Category() // Added passedCategory parameter
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CategoryDetailScreenUiState(
            category = passedCategory,
            isEditing = passedCategory.uuid.isNotEmpty()
        )
    )
    val uiState: StateFlow<CategoryDetailScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firestoreViewModel.keywords.collect { keywords ->
                _uiState.value = _uiState.value.copy(keywords = keywords)
            }
        }
    }

    fun onEvent(event: CategoryDetailScreenEvent) {
        when (event) {
            is CategoryDetailScreenEvent.OnCategoryNameChanged -> onCategoryNameChanged(event.name)
            is CategoryDetailScreenEvent.DuplicateKeywordChecker -> duplicateKeywordChecker(event.keyword)
            is CategoryDetailScreenEvent.OnKeywordAdded -> onKeywordAdded(event.keyword)
            is CategoryDetailScreenEvent.OnKeywordRemoved -> onKeywordRemoved(event.keyword)
            is CategoryDetailScreenEvent.OnCategoryTransactionTypeChangedShowDropdownDialogWithTransactionTypeEnumChoice ->
                onCategoryTransactionTypeChangedShowDropdownDialogWithTransactionTypeEnumChoice(event.transactionTypeOrdinal)
            is CategoryDetailScreenEvent.OnCategoryColorChanged -> onCategoryColorChanged(event.selectedColor)
            is CategoryDetailScreenEvent.OnPerformSaveCategoryWhenItNotEmpty -> onPerformSaveCategoryWhenItNotEmpty(event.category)
            is CategoryDetailScreenEvent.OnPerformUpdateCategoryWhenItChangedAndNotEmpty ->
                onPerformUpdateCategoryWhenItChangedAndNotEmpty(event.category)
            is CategoryDetailScreenEvent.OnSoftDeleteButtonClicked -> onSoftDeleteButtonClicked(event.categoryUuid)
        }
    }

    private fun onCategoryNameChanged(name: String) {
        // Update category name in uiState
    }

    private fun duplicateKeywordChecker(keyword: String) {
        // Check if keyword already exists
    }

    private fun onKeywordAdded(keyword: String) {
        // Add keyword to uiState.category.keywords
    }

    private fun onKeywordRemoved(keyword: String) {
        // Remove keyword from uiState.category.keywords
    }

    private fun onCategoryTransactionTypeChangedShowDropdownDialogWithTransactionTypeEnumChoice(transactionTypeOrdinal: Int) {
        // Show dropdown dialog to change transaction type
    }

    private fun onCategoryColorChanged(selectedColor: String) {
        // Update category color in uiState
    }

    private fun onPerformSaveCategoryWhenItNotEmpty(category: Category) {
        // Save category to Firestore
    }

    private fun onPerformUpdateCategoryWhenItChangedAndNotEmpty(category: Category) {
        // Update category in Firestore
    }

    private fun onSoftDeleteButtonClicked(categoryUuid: String) {
        // Soft delete category from Firestore
    }
}