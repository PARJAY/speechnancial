package com.example.speechnancial.data.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Keyword
import com.example.speechnancial.data.firebase.model.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WalletDetailScreenEvent {
    data class OnWalletNameChanged(val name: String) : WalletDetailScreenEvent()
    data class OnSetInitialBalance(val balance: Float) : WalletDetailScreenEvent()
    data object OnOpenTransactionScreenAndFilterTransactionByThisWallet : WalletDetailScreenEvent()
    data object OnWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData : WalletDetailScreenEvent()
    data class DuplicateKeywordChecker(val keyword: String) : WalletDetailScreenEvent()
    data class OnWalletIsDefaultChanged(val isDefault: Boolean) : WalletDetailScreenEvent()
//    data class OnWalletColorChanged(val selectedColor: String) : WalletDetailScreenEvent()
    data class OnPerformSaveWalletWhenItNotEmpty(val wallet: Wallet) : WalletDetailScreenEvent()
    data class OnPerformUpdateWalletWhenItChangedAndNotEmpty(val wallet: Wallet) : WalletDetailScreenEvent()
    data class OnSoftDeleteButtonClicked(val walletUuid: String) : WalletDetailScreenEvent()
}

data class WalletDetailScreenUiState(
    val wallet: Wallet = Wallet(),
    val keywords: List<Keyword> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false
)

class WalletDetailScreenUIViewModel(
    private val firestoreViewModel: FirestoreCollectionViewModel,
    passedWallet: Wallet = Wallet() // Added passedWallet parameter
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalletDetailScreenUiState(wallet = passedWallet)) // Initialize with passedWallet
    val uiState: StateFlow<WalletDetailScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firestoreViewModel.keywords.collect { keywords ->
                _uiState.value = _uiState.value.copy(keywords = keywords)
            }
        }
    }

    fun onEvent(event: WalletDetailScreenEvent) {
        when (event) {
            is WalletDetailScreenEvent.OnWalletNameChanged -> onWalletNameChanged(event.name)
            is WalletDetailScreenEvent.OnSetInitialBalance -> onSetInitialBalance(event.balance)
            WalletDetailScreenEvent.OnOpenTransactionScreenAndFilterTransactionByThisWallet -> onOpenTransactionScreenAndFilterTransactionByThisWallet()
            WalletDetailScreenEvent.OnWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData -> onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData()
            is WalletDetailScreenEvent.DuplicateKeywordChecker -> duplicateKeywordChecker(event.keyword)
            is WalletDetailScreenEvent.OnWalletIsDefaultChanged -> onWalletIsDefaultChanged(event.isDefault)
//            is WalletDetailScreenEvent.OnWalletColorChanged -> onWalletColorChanged(event.selectedColor)
            is WalletDetailScreenEvent.OnPerformSaveWalletWhenItNotEmpty -> onPerformSaveWalletWhenItNotEmpty(event.wallet)
            is WalletDetailScreenEvent.OnPerformUpdateWalletWhenItChangedAndNotEmpty -> onPerformUpdateWalletWhenItChangedAndNotEmpty(event.wallet)
            is WalletDetailScreenEvent.OnSoftDeleteButtonClicked -> onSoftDeleteButtonClicked(event.walletUuid)
        }
    }

    private fun onWalletNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(isEditing = true, wallet = _uiState.value.wallet.copy(name = name))
    }

    private fun onSetInitialBalance(balance: Float) {
        _uiState.value = _uiState.value.copy(isEditing = true, wallet = _uiState.value.wallet.copy(balance = balance))
    }

    private fun onOpenTransactionScreenAndFilterTransactionByThisWallet() {
        // Implement navigation to transaction screen with filter
    }

    private fun onWalletOpnameBalanceOpenInputTransactionScreenWithOpnameWalletData() {
        // Implement navigation to input transaction screen with opname data
    }

    private fun duplicateKeywordChecker(keyword: String) {
        // Implement keyword duplication check
    }

    private fun onWalletIsDefaultChanged(isDefault: Boolean) {
        _uiState.value = _uiState.value.copy(isEditing = true, wallet = _uiState.value.wallet.copy(isDefaultWallet = isDefault))
    }

//    private fun onWalletColorChanged(selectedColor: String) {
//        _uiState.value = _uiState.value.copy(isEditing = true, wallet = _uiState.value.wallet.copy(color = selectedColor))
//    }

    private fun onPerformSaveWalletWhenItNotEmpty(wallet: Wallet) {
        // Implement save wallet logic
    }

    private fun onPerformUpdateWalletWhenItChangedAndNotEmpty(wallet: Wallet) {
        // Implement update wallet logic
    }

    private fun onSoftDeleteButtonClicked(walletUuid: String) {
        // Implement soft delete wallet logic
    }
}