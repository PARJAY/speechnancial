package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.repository.DebtAndReceivableRepository
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DebtAndReceivableScreenEvent {
    data class OnDebtAndReceivableItemClickOpenDebtAndReceivableDetailScreenWithData(val debtAndReceivable: DebtAndReceivable) : DebtAndReceivableScreenEvent()

    data class OnSearchQueryChange(val query: String) : DebtAndReceivableScreenEvent()

    data object OpenDialog : DebtAndReceivableScreenEvent()
    data object CloseDialog : DebtAndReceivableScreenEvent()
    data class SetSelectedDebtAndReceivable(val debtAndReceivable: DebtAndReceivable) : DebtAndReceivableScreenEvent()
    data object ResetSelectedDebtAndReceivable : DebtAndReceivableScreenEvent()

    data class SaveDebtAndReceivable(val debtAndReceivable: DebtAndReceivable, val context: Context) : DebtAndReceivableScreenEvent()
    data class UpdateDebtAndReceivable(val updatedDebtAndReceivable: DebtAndReceivable, val context: Context) : DebtAndReceivableScreenEvent()
    data class SoftDeleteDebtAndReceivable(val debtAndReceivableUuid: String, val context: Context) : DebtAndReceivableScreenEvent()
    data class HardDeleteDebtAndReceivable(val debtAndReceivableUuid: String, val context: Context) : DebtAndReceivableScreenEvent()

    data class ShowToast(val message: String) : DebtAndReceivableScreenEvent()
}

data class DebtAndReceivableScreenUiState(
    val debtAndReceivables: List<DebtAndReceivable> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val showDialogDebtAndReceivableCrud: Boolean = false,
    val selectedDebtAndReceivable: DebtAndReceivable = DebtAndReceivable(),

    val toastMessage: String? = null,
    val searchQuery: String = ""
)

class DebtAndReceivableUIViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val repository: DebtAndReceivableRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DebtAndReceivableScreenUiState())
    val uiState: StateFlow<DebtAndReceivableScreenUiState> = _uiState.asStateFlow()

    private val allDebtAndReceivables = MutableStateFlow<List<DebtAndReceivable>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.debtAndReceivables.collect { debtAndReceivables ->
                allDebtAndReceivables.value = debtAndReceivables
                filterDebtAndReceivables(_uiState.value.searchQuery)
            }
        }
    }

    private fun filterDebtAndReceivables(query: String) {
        val filtered = if (query.isBlank()) {
            allDebtAndReceivables.value
        } else {
            allDebtAndReceivables.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(debtAndReceivables = filtered) }
    }

    fun onEvent(event: DebtAndReceivableScreenEvent) {
        when (event) {
            is DebtAndReceivableScreenEvent.OnDebtAndReceivableItemClickOpenDebtAndReceivableDetailScreenWithData -> {
                _uiState.update { it.copy(selectedDebtAndReceivable = event.debtAndReceivable) }
                onEvent(DebtAndReceivableScreenEvent.OpenDialog)
            }

            is DebtAndReceivableScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterDebtAndReceivables(event.query)
            }

            DebtAndReceivableScreenEvent.OpenDialog -> _uiState.update { it.copy(showDialogDebtAndReceivableCrud = true) }
            DebtAndReceivableScreenEvent.CloseDialog -> _uiState.update { it.copy(showDialogDebtAndReceivableCrud = false) }

            is DebtAndReceivableScreenEvent.SetSelectedDebtAndReceivable -> {
                _uiState.update { it.copy(selectedDebtAndReceivable = event.debtAndReceivable) }
            }

            DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable -> {
                _uiState.update {
                    it.copy(
                        selectedDebtAndReceivable = DebtAndReceivable()
                    )
                }
            }

            is DebtAndReceivableScreenEvent.SaveDebtAndReceivable -> {
                saveDebtAndReceivable(event.debtAndReceivable, event.context)
            }
            is DebtAndReceivableScreenEvent.UpdateDebtAndReceivable -> {
                updateDebtAndReceivable(event.updatedDebtAndReceivable, event.context)
            }
            is DebtAndReceivableScreenEvent.SoftDeleteDebtAndReceivable -> {
                softDeleteDebtAndReceivable(event.debtAndReceivableUuid, event.context)
            }
            is DebtAndReceivableScreenEvent.HardDeleteDebtAndReceivable -> {
                hardDeleteDebtAndReceivable(event.debtAndReceivableUuid, event.context)
            }

            is DebtAndReceivableScreenEvent.ShowToast -> {
                _uiState.update { it.copy(toastMessage = event.message) }
            }
        }
    }

    private fun saveDebtAndReceivable(debtAndReceivable: DebtAndReceivable, context: Context) {
        viewModelScope.launch {
            repository.addDebtAndReceivable(
                userId,
                debtAndReceivable,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("DebtVM", "Debt/Receivable ADDED: ${debtAndReceivable.uuid}")
                    logDataFlowToFile("DebtVM", "full Debt/Receivable: $debtAndReceivable")
                    logDataFlowToFile("DebtVM", "")
                }
            )
        }
        onEvent(DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable)
        onEvent(DebtAndReceivableScreenEvent.CloseDialog)
    }

    private fun updateDebtAndReceivable(debtAndReceivable: DebtAndReceivable, context: Context) {
        val old = uiState.value.debtAndReceivables.find { it.uuid == debtAndReceivable.uuid }

        viewModelScope.launch {
            repository.updateDebtAndReceivable(
                userId,
                debtAndReceivable,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("DebtVM", "Debt/Receivable UPDATED: ${debtAndReceivable.uuid}")
                    logDataFlowToFile("DebtVM", "from: $old")
                    logDataFlowToFile("DebtVM", "to:   $debtAndReceivable")
                    logDataFlowToFile("DebtVM", "")
                }
            )
        }
        onEvent(DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable)
        onEvent(DebtAndReceivableScreenEvent.CloseDialog)
    }

    private fun hardDeleteDebtAndReceivable(debtAndReceivableUuid: String, context: Context) {
        viewModelScope.launch {
            repository.deleteDebtAndReceivable(
                userId,
                debtAndReceivableUuid,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("DebtVM", "Debt/Receivable DELETED: $debtAndReceivableUuid")
                    logDataFlowToFile("DebtVM", "")
                }
            )
        }
        onEvent(DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable)
        onEvent(DebtAndReceivableScreenEvent.CloseDialog)
    }

    private fun softDeleteDebtAndReceivable(debtAndReceivableUuid: String, context: Context) {
        viewModelScope.launch {
            repository.softDeleteDebtAndReceivable(
                userId,
                debtAndReceivableUuid,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            )
        }
        onEvent(DebtAndReceivableScreenEvent.ResetSelectedDebtAndReceivable)
        onEvent(DebtAndReceivableScreenEvent.CloseDialog)
    }
}