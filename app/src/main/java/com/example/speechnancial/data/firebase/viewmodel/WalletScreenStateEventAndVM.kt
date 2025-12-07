package com.example.speechnancial.data.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.repository.WalletRepository
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class WalletScreenEvent {
    data class OnWalletItemClickOpenWalletDetailScreenWithData(val wallet: Wallet) : WalletScreenEvent()

    data class OnSearchQueryChange(val query: String) : WalletScreenEvent()

    data class SaveWallet(val wallet: Wallet, val callback: (String) -> Unit) : WalletScreenEvent()
    data class UpdateWallet(val wallet: Wallet, val callback: (String) -> Unit) : WalletScreenEvent()
    data class DeleteWallet(val walletUuid: String, val callback: (String) -> Unit) : WalletScreenEvent()

    data object OpenDialog : WalletScreenEvent()
    data object CloseDialog : WalletScreenEvent()

    data class SetSelectedWallet(val wallet: Wallet) : WalletScreenEvent()
    data object ClearSelectedWallet : WalletScreenEvent()
}

data class WalletScreenUiState(
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDialogWalletCrud: Boolean = false,
    val selectedWallet: Wallet = Wallet(),
    val searchQuery: String = ""
)

class WalletListScreenUIViewModel(
    private val userId: String,
    private val firestoreViewModel: FirestoreCollectionViewModel,
    private val walletRepository: WalletRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalletScreenUiState())
    val uiState: StateFlow<WalletScreenUiState> = _uiState.asStateFlow()

    private val allWallets = MutableStateFlow<List<Wallet>>(emptyList())

    init {
        viewModelScope.launch {
            firestoreViewModel.wallets.collect { wallets ->
                allWallets.value = wallets.sortedByDescending { it.isDefaultWallet }
                filterWallets(_uiState.value.searchQuery)
            }
        }
    }

    private fun filterWallets(query: String) {
        val filtered = if (query.isBlank()) {
            allWallets.value
        } else {
            allWallets.value.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }.sortedByDescending { it.isDefaultWallet }

        _uiState.update { it.copy(wallets = filtered) }
    }

    fun onEvent(event: WalletScreenEvent) {
        when (event) {
            is WalletScreenEvent.OnWalletItemClickOpenWalletDetailScreenWithData -> {
                _uiState.update { it.copy(selectedWallet = event.wallet) }
                onEvent(WalletScreenEvent.OpenDialog)
            }

            is WalletScreenEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                filterWallets(event.query)
            }

            is WalletScreenEvent.SaveWallet -> {
                saveWallet(event.wallet, event.callback)
                onEvent(WalletScreenEvent.CloseDialog)
            }
            is WalletScreenEvent.UpdateWallet -> {
                updateWallet(event.wallet, event.callback)
                onEvent(WalletScreenEvent.CloseDialog)
            }
            is WalletScreenEvent.DeleteWallet -> {
                deleteWallet(event.walletUuid, event.callback)
                onEvent(WalletScreenEvent.CloseDialog)
            }

            WalletScreenEvent.OpenDialog -> {
                _uiState.update { it.copy(showDialogWalletCrud = true) }
            }
            WalletScreenEvent.CloseDialog -> {
                _uiState.update { it.copy(showDialogWalletCrud = false) }
            }
            is WalletScreenEvent.SetSelectedWallet -> {
                _uiState.update { it.copy(selectedWallet = event.wallet) }
                onEvent(WalletScreenEvent.OpenDialog)
            }

            WalletScreenEvent.ClearSelectedWallet -> {
                _uiState.update { it.copy(selectedWallet = Wallet()) }
            }
        }
    }

    private fun saveWallet(wallet: Wallet, callback: (String) -> Unit) {
        viewModelScope.launch {
            val isFirstWallet = uiState.value.wallets.isEmpty()

            val walletToSave = if (isFirstWallet) {
                wallet.copy(isDefaultWallet = true)
            } else {
                wallet
            }

            walletRepository.addWallet(
                userId,
                walletToSave,
                callback = { message ->
                    logDataFlowToFile("WalletVM", "Wallet ADDED: ${walletToSave.uuid}")
                    logDataFlowToFile("WalletVM", "full Wallet: $walletToSave")
                    logDataFlowToFile("WalletVM", "")
                    callback(message)
                }
            )
        }
    }

    private fun updateWallet(wallet: Wallet, callback: (String) -> Unit) {
        val old = uiState.value.wallets.find { it.uuid == wallet.uuid }

        viewModelScope.launch {
            walletRepository.updateWallet(
                userId,
                wallet,
                callback = { message ->
                    logDataFlowToFile("WalletVM", "Wallet UPDATED: ${wallet.uuid}")
                    logDataFlowToFile("WalletVM", "from: $old")
                    logDataFlowToFile("WalletVM", "to:   $wallet")
                    logDataFlowToFile("WalletVM", "")
                    callback(message)
                }
            )

            // If set to be the default wallet, reset other wallets
            if (wallet.isDefaultWallet) {
                _uiState.value.wallets.forEach { otherWallet ->
                    if (otherWallet.uuid != wallet.uuid && otherWallet.isDefaultWallet) {
                        val updatedOtherWallet = otherWallet.copy(isDefaultWallet = false)
                        walletRepository.updateWallet(
                            userId,
                            updatedOtherWallet,
                            callback = { message ->
                                logDataFlowToFile("WalletVM", "Unset isDefaultWallet on wallet: ${updatedOtherWallet.uuid}")
                                logDataFlowToFile("WalletVM", "full Wallet: $updatedOtherWallet")
                                logDataFlowToFile("WalletVM", "")
                                callback(message)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun deleteWallet(walletUuid: String, callback: (String) -> Unit) {
        viewModelScope.launch {
            walletRepository.deleteWallet(
                userId,
                walletUuid, callback = { message ->
                logDataFlowToFile("WalletVM", "Wallet DELETED: $walletUuid")
                logDataFlowToFile("WalletVM", "")
                callback(message)
            })
        }
    }
}