package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.repository.SavingRepository
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SavingScreenEvent {
    data class OnSavingItemClickOpenSavingDetailScreenWithData(val saving: Saving) : SavingScreenEvent()

    data class OnSearchQueryChange(val query: String) : SavingScreenEvent()

    data object OpenDialog : SavingScreenEvent()
    data object CloseDialog : SavingScreenEvent()
    data class SetSelectedSaving(val saving: Saving) : SavingScreenEvent()
    data object ResetSelectedSaving : SavingScreenEvent()

    data class SaveSaving(val saving: Saving, val context: Context) : SavingScreenEvent()
    data class UpdateSaving(val updatedSaving: Saving, val context: Context) : SavingScreenEvent()
    data class DeleteSaving(val savingUuid: String, val context: Context) : SavingScreenEvent()

    data class ShowToast(val message: String) : SavingScreenEvent()
}

data class SavingScreenUiState(
    val savings: List<Saving> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val showDialogSavingCrud: Boolean = false,
    val selectedSaving: Saving = Saving(),

    val toastMessage: String? = null,
    val searchQuery: String = ""
)

class SavingListScreenUIViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val repository: SavingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavingScreenUiState())
    val uiState: StateFlow<SavingScreenUiState> = _uiState.asStateFlow()

    private val allSavings = MutableStateFlow<List<Saving>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.savings.collect { savings ->
                allSavings.value = savings
                filterSavings(_uiState.value.searchQuery)
            }
        }
    }

    private fun filterSavings(query: String) {
        val filtered = if (query.isBlank()) {
            allSavings.value
        } else {
            allSavings.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(savings = filtered) }
    }

    fun onEvent(event: SavingScreenEvent) {
        when (event) {
            is SavingScreenEvent.OnSavingItemClickOpenSavingDetailScreenWithData -> {
                _uiState.update { it.copy(selectedSaving = event.saving) }
                onEvent(SavingScreenEvent.OpenDialog)
            }

            is SavingScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterSavings(event.query)
            }

            SavingScreenEvent.OpenDialog -> _uiState.update { it.copy(showDialogSavingCrud = true) }
            SavingScreenEvent.CloseDialog -> _uiState.update { it.copy(showDialogSavingCrud = false) }

            is SavingScreenEvent.SetSelectedSaving -> {
                _uiState.update { it.copy(selectedSaving = event.saving) }
            }

            SavingScreenEvent.ResetSelectedSaving -> {
                _uiState.update {
                    it.copy(
                        selectedSaving = Saving() // saving kosong
                    )
                }
            }

            is SavingScreenEvent.SaveSaving -> {
                saveSaving(event.saving, event.context)
            }
            is SavingScreenEvent.UpdateSaving -> {
                updateSaving(event.updatedSaving, event.context)
            }
            is SavingScreenEvent.DeleteSaving -> {
                deleteSaving(event.savingUuid, event.context)
            }

            is SavingScreenEvent.ShowToast -> {
                _uiState.update { it.copy(toastMessage = event.message) }
            }
        }
    }

    private fun saveSaving(saving: Saving, context: Context) {
        viewModelScope.launch {
            repository.addSaving(
                userId,
                saving,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("SavingVM", "Saving ADDED: ${saving.uuid}")
                    logDataFlowToFile("SavingVM", "full Saving: $saving")
                    logDataFlowToFile("SavingVM", "")
                }
            )
        }
        onEvent(SavingScreenEvent.ResetSelectedSaving)
        onEvent(SavingScreenEvent.CloseDialog)
    }

    private fun updateSaving(saving: Saving, context: Context) {
        val old = uiState.value.savings.find { it.uuid == saving.uuid }

        viewModelScope.launch {
            repository.updateSaving(
                userId,
                saving,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("SavingVM", "Saving UPDATED: ${saving.uuid}")
                    logDataFlowToFile("SavingVM", "from: $old")
                    logDataFlowToFile("SavingVM", "to:   $saving")
                    logDataFlowToFile("SavingVM", "")
                }
            )
        }
        onEvent(SavingScreenEvent.ResetSelectedSaving)
        onEvent(SavingScreenEvent.CloseDialog)
    }

    private fun deleteSaving(savingUuid: String, context: Context) {
        viewModelScope.launch {
            repository.deleteSaving(
                userId,
                savingUuid,
                callback = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    logDataFlowToFile("SavingVM", "Saving DELETED: $savingUuid")
                    logDataFlowToFile("SavingVM", "")
                }
            )
        }
        onEvent(SavingScreenEvent.ResetSelectedSaving)
        onEvent(SavingScreenEvent.CloseDialog)
    }
}