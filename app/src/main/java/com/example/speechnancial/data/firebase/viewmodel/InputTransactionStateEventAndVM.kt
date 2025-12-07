package com.example.speechnancial.data.firebase.viewmodel

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.repository.BudgetRepository
import com.example.speechnancial.data.firebase.repository.DebtAndReceivableRepository
import com.example.speechnancial.data.firebase.repository.SavingRepository
import com.example.speechnancial.data.firebase.repository.TransactionRepository
import com.example.speechnancial.data.firebase.repository.WalletRepository
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import com.example.speechnancial.tools.algoritma.working.inputtedTextToTransactionsConverter
import com.example.speechnancial.tools.startSpeechToText
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

// todo :
//  setiap create transaksi akan melibatkan wallet total spending, total earning, balance dan mungkin juga budget remainingAmount
//  sebagai gantinya total income / total expense / total balance wallet yang dipilih pada transaksi
//  yang akan di update ballancenya

data class ProposedTransactionUiState(
    val transaction: Transaction,
    val walletName: String? = "",
    val relatedItemKindName: String? = ""
)

data class InputTransactionState(
    // data
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val debtReceivables: List<DebtAndReceivable> = emptyList(),
    val savings: List<Saving> = emptyList(),
    val budgets: List<Budget> = emptyList(),

    // screen data
    val proposedTransactions: List<ProposedTransactionUiState> = listOf(),
    val previousPartialResult: String = "",
    val source: String = "",
    val isTranscribing: Boolean = false,
    val isFinishedTranscribing: Boolean = true,
    val isReviseNeeded: Boolean = false,
    val isTranscriptionError: Boolean = false,

    // Dialog Visibility States
    val showTransactionKindDialog: Boolean = false,
    val showSelectWalletDialog: Boolean = false,
    val showRelatedItemDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,

    // Selected Data
    val currentTransactionPosition: Int = 0,

    // this 2 is used to contain the selected value of item for each transaction position
    val selectedWalletName: List<String> = listOf(""),
    val selectedRelatedItemKindName: List<String> = listOf(""),
)

// karena kita akan membuat dialog nantinya
sealed interface InputTransactionEvent {
    data class IsEditExistingTransactionData(val transaction : Transaction): InputTransactionEvent
    data class IsCloseScreen(val navController: NavHostController) : InputTransactionEvent

    data class SpeechToTransactionButtonClicked(val context: Context) : InputTransactionEvent
    data object ResetInput : InputTransactionEvent
    data class HandleUserInput(val userInput: String) : InputTransactionEvent
    data object OnFinishInputtingMakeTransactions : InputTransactionEvent

    data object ReviseLaterCheckboxClicked : InputTransactionEvent
    data object TranscriptionErrorCheckboxClicked : InputTransactionEvent

    // Transaction Kind Dialog
    data object OpenTransactionKindDialog : InputTransactionEvent
    data object HideTransactionKindDialog : InputTransactionEvent
    data class SelectTransactionKind(val transactionType: TransactionType) : InputTransactionEvent // Changed to accept TransactionKind enum

    // Select Wallet Dialog
    data object OpenSelectWalletDialog : InputTransactionEvent
    data object HideSelectWalletDialog : InputTransactionEvent
    data class SelectWallet(val wallet: Wallet) : InputTransactionEvent

    // Related Item Dialog
    data object OpenRelatedItemDialog : InputTransactionEvent
    data object HideRelatedItemDialog : InputTransactionEvent
    data class SelectRelatedItem(val kindUuid: String, val kind: String) : InputTransactionEvent // Added 'kind' (category, saving, etc.)
    data object ClearRelatedItemDialog : InputTransactionEvent

    // Date Picker Dialog
    data object OpenDatePickerDialog : InputTransactionEvent
    data object HideDatePickerDialog : InputTransactionEvent
    data class SelectDate(val date: Timestamp) : InputTransactionEvent

    // current position is handled in state
    data class ChangeCurrentTransactionPosition(val position: Int) : InputTransactionEvent

    data object SaveTransaction : InputTransactionEvent
    data object UpdateTransaction : InputTransactionEvent
    data object DeleteTransaction : InputTransactionEvent
}

class InputTransactionViewModel(
    private val userId: String,
    private val firestoreCollectionViewModel: FirestoreCollectionViewModel,
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
    private val budgetRepository: BudgetRepository,
    private val savingRepository: SavingRepository,
    private val debtReceivableRepository: DebtAndReceivableRepository,
    context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(InputTransactionState())
    val state = _state.asStateFlow()

    private val _speechRecognizer = MutableStateFlow(SpeechRecognizer.createSpeechRecognizer(context))

    private val _speechRecognizerIntent = MutableStateFlow(
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
        }
    )

    private val TAG = "InputTransactionVM" // Tag untuk Log

    init {
        logDataFlowToFile(TAG, "")
        logDataFlowToFile(TAG, "")
        logDataFlowToFile(TAG, "")
        logDataFlowToFile(TAG, "Entering $TAG Screen")
        logDataFlowToFile(TAG, "")

        viewModelScope.launch {
            firestoreCollectionViewModel.categories.collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }

        viewModelScope.launch {
            firestoreCollectionViewModel.wallets.collect { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }

        viewModelScope.launch {
            firestoreCollectionViewModel.budgets.collect { budgets ->
                _state.update { it.copy(budgets = budgets) }
            }
        }

        viewModelScope.launch {
            firestoreCollectionViewModel.savings.collect { savings ->
                _state.update { it.copy(savings = savings) }
            }
        }

        viewModelScope.launch {
            firestoreCollectionViewModel.debtAndReceivables.collect { debtReceivables ->
                _state.update { it.copy(debtReceivables = debtReceivables) }
            }
        }

        viewModelScope.launch {
            firestoreCollectionViewModel.selectedTransaction.collectLatest { transactionToEdit ->
                transactionToEdit.let {
                    if (it.uuid.isNotEmpty()) {
                        Log.d(TAG, "$it")
                        onEvent(InputTransactionEvent.IsEditExistingTransactionData(it))
//                        firestoreCollectionViewModel.setSelectedTransaction(Transaction())
                    }
                }
            }
        }
    }

    fun onEvent(event: InputTransactionEvent) {
        when (event) {
            is InputTransactionEvent.IsEditExistingTransactionData -> {
                val detailsText =
                    event.transaction.details?.entries?.joinToString(separator = "\n") { (description, nominal) ->
                        "$description ${nominal.toInt()} rupiah"
                    }?.ifEmpty { event.transaction.fullText } ?: event.transaction.fullText

                val walletName = state.value.wallets.find { it.uuid == event.transaction.relatedWalletUuid }?.name

                val relatedItemKindName = when (event.transaction.transactionTypeOrdinal) {
                    TransactionType.EXPENSE.ordinal,
                    TransactionType.INCOME.ordinal -> state.value.categories.find { it.uuid == event.transaction.relatedEntityId }?.name
                    TransactionType.REGULAR_TRANSFER.ordinal -> state.value.wallets.find { it.uuid == event.transaction.relatedEntityId }?.name
                    TransactionType.SAVING_DEPOSIT.ordinal,
                    TransactionType.SAVING_WITHDRAWAL.ordinal -> state.value.savings.find { it.uuid == event.transaction.relatedEntityId }?.name
                    TransactionType.DEBT_PAYMENT.ordinal,
                    TransactionType.RECEIVABLE_PAYMENT.ordinal -> state.value.debtReceivables.find { it.uuid == event.transaction.relatedEntityId }?.name
                    else -> null
                }

                _state.update {
                    it.copy(
                        proposedTransactions = listOf(ProposedTransactionUiState(
                            transaction = event.transaction.copy(isFromSmartwatch = false),
                            walletName = walletName,
                            relatedItemKindName = relatedItemKindName,
                        )),
                        source = detailsText,
                    )
                }

                if (event.transaction.isFromSmartwatch) {
                    onEvent(InputTransactionEvent.OnFinishInputtingMakeTransactions)
                }
            }

            is InputTransactionEvent.IsCloseScreen -> {
                _state.update { currentState ->
                    currentState.copy(
                        proposedTransactions = listOf(),
                        previousPartialResult = "",
                        source = "",
                        isTranscribing = false,
                        isFinishedTranscribing = true,
                        isReviseNeeded = false,
                        isTranscriptionError = false,

                        showTransactionKindDialog = false,
                        showSelectWalletDialog = false,
                        showRelatedItemDialog = false,
                        showDatePickerDialog = false,
                    )
                }

                firestoreCollectionViewModel.setSelectedTransaction(Transaction())
                event.navController.popBackStack()
            }

            is InputTransactionEvent.SpeechToTransactionButtonClicked -> {
                _state.update { currentState ->
                    if (!currentState.isTranscribing) {
                        currentState.copy(
                            isTranscribing = true,
                            isFinishedTranscribing = false
                        )
                    } else {
                        currentState.copy(
                            isTranscribing = false,
                            isFinishedTranscribing = true
                        )
                    }
                }

                if (_state.value.isTranscribing) {
                    startSpeechToText(
                        context = event.context,
                        speechRecognizer = _speechRecognizer.value,
                        speechRecognizerIntent = _speechRecognizerIntent.value,
                        onPartialResults = { partialResult ->
                            _state.update { it.copy(
                                source =
                                if (it.previousPartialResult.isNotEmpty() && partialResult.isEmpty())
                                    it.source + it.previousPartialResult
                                else it.source,
                                previousPartialResult = partialResult,
                            ) }
                        }
                    )

                    _speechRecognizer.value.startListening(_speechRecognizerIntent.value)

                } else {
                    _speechRecognizer.value.stopListening()
                    _speechRecognizer.value.destroy()

                    _state.update { it.copy(
                        source = it.source + it.previousPartialResult,
                        previousPartialResult = ""
                    ) }
                    onEvent(InputTransactionEvent.OnFinishInputtingMakeTransactions)
                }
            }

            InputTransactionEvent.ResetInput -> _state.update { currentState ->
                currentState.copy(
                    proposedTransactions = emptyList(),
                    previousPartialResult = "",
                    source = "",
                    isTranscribing = false,
                    isFinishedTranscribing = true,
                    isReviseNeeded = false,
                    isTranscriptionError = false
                )
            }

            is InputTransactionEvent.HandleUserInput -> {
                _state.update { it.copy(
                    source = event.userInput,
                    previousPartialResult = ""
                ) }
            }

            InputTransactionEvent.OnFinishInputtingMakeTransactions -> {
                val sourceText = state.value.source.lowercase()

                // Konversi input menjadi daftar transaksi
                val convertedTransactions = inputtedTextToTransactionsConverter(
                    sourceText,
                    boyerMooreMultiplePatternsWithReturn(sourceText)
                )

                // Ambil UUID dari transaksi yang dipilih (jika ada)
                // lakukan pengecekan apakah id kosong?
                // jika iya maka tidak perlu menjalankan yang dibawah ini
                // tujuan dari yang dibawah ini hanyalah agar user tidak perlu menekan banyak tombol detail transaksi lagi saat edit data

                val selectedTransaction = firestoreCollectionViewModel.selectedTransaction.value
                val isEditing = selectedTransaction.uuid.isNotBlank()

                val proposedUiStates = if (isEditing) {
                    val walletName = state.value.wallets.find { it.uuid == selectedTransaction.relatedWalletUuid }?.name
                    val relatedItemKindName = when (selectedTransaction.transactionTypeOrdinal) {
                        TransactionType.EXPENSE.ordinal,
                        TransactionType.INCOME.ordinal -> state.value.categories.find { it.uuid == selectedTransaction.relatedEntityId }?.name
                        TransactionType.REGULAR_TRANSFER.ordinal -> state.value.wallets.find { it.uuid == selectedTransaction.relatedEntityId }?.name
                        TransactionType.SAVING_DEPOSIT.ordinal,
                        TransactionType.SAVING_WITHDRAWAL.ordinal -> state.value.savings.find { it.uuid == selectedTransaction.relatedEntityId }?.name
                        TransactionType.DEBT_PAYMENT.ordinal,
                        TransactionType.RECEIVABLE_PAYMENT.ordinal -> state.value.debtReceivables.find { it.uuid == selectedTransaction.relatedEntityId }?.name
                        else -> null
                    }

                    convertedTransactions.mapIndexed { index, transaction ->
                        val updatedTransaction = if (index == 0) {
                            transaction.copy(
                                uuid = selectedTransaction.uuid,
                                relatedWalletUuid = selectedTransaction.relatedWalletUuid,
                                relatedEntityId = selectedTransaction.relatedEntityId,
                                dateAdded = selectedTransaction.dateAdded // penting: agar tidak overwrite date lama saat edit
                            )
                        } else transaction

                        ProposedTransactionUiState(
                            transaction = updatedTransaction,
                            walletName = walletName,
                            relatedItemKindName = relatedItemKindName
                        )
                    }
                } else {
                    convertedTransactions.mapIndexed { index, transaction ->
                        val timestamp = Timestamp.now()
                        val updatedTransaction = transaction.copy(dateAdded = timestamp)

                        ProposedTransactionUiState(
                            transaction = updatedTransaction,
                            walletName = "", // default value
                            relatedItemKindName = "" // default value
                        )
                    }
                }


                // Update state dengan hasil konversi dan pemetaan
                _state.update {
                    it.copy(proposedTransactions = proposedUiStates)
                }
            }

            // this 2 might useless
            InputTransactionEvent.ReviseLaterCheckboxClicked -> {
                _state.update { it.copy(isReviseNeeded = !it.isReviseNeeded) }
            }
            InputTransactionEvent.TranscriptionErrorCheckboxClicked -> {
                _state.update { it.copy(isTranscriptionError = !it.isTranscriptionError) }
            }

            // Transaction Kind Dialog
            InputTransactionEvent.OpenTransactionKindDialog -> {
                _state.update { it.copy(showTransactionKindDialog = true) }
            }
            InputTransactionEvent.HideTransactionKindDialog -> {
                _state.update { it.copy(showTransactionKindDialog = false) }
            }
            is InputTransactionEvent.SelectTransactionKind -> {
                _state.update { currentState ->
                    currentState.proposedTransactions.toMutableList().apply {
                        val currentPos = currentState.currentTransactionPosition
                        getOrNull(currentPos)?.let { currentUiState ->
                            this[currentPos] = currentUiState.copy(
                                transaction = currentUiState.transaction.copy(
                                    transactionTypeOrdinal = event.transactionType.ordinal
                                ),
                                relatedItemKindName = null
                            )
                        }
                    }.let { currentState.copy(proposedTransactions = it) }
                }
            }

            // Select Wallet Dialog
            InputTransactionEvent.OpenSelectWalletDialog -> {
                _state.update { it.copy(showSelectWalletDialog = true) }
            }
            InputTransactionEvent.HideSelectWalletDialog -> {
                _state.update { it.copy(showSelectWalletDialog = false) }
            }
            is InputTransactionEvent.SelectWallet -> {
                _state.update { currentState ->
                    val currentPos = currentState.currentTransactionPosition
                    currentState.proposedTransactions.toMutableList().apply {
                        getOrNull(currentPos)?.let { currentUiState ->
                            this[currentPos] = currentUiState.copy(
                                transaction = currentUiState.transaction.copy(
                                    relatedWalletUuid = event.wallet.uuid
                                ),
                                walletName = event.wallet.name
                            )
                        }
                    }.let { updatedUiStates ->
                        currentState.copy(proposedTransactions = updatedUiStates)
                    }
                }
                _state.update { it.copy(showSelectWalletDialog = false) }
            }

            // Related Item Dialog
            InputTransactionEvent.OpenRelatedItemDialog -> {
                _state.update { it.copy(showRelatedItemDialog = true) }
            }
            InputTransactionEvent.HideRelatedItemDialog -> {
                _state.update { it.copy(showRelatedItemDialog = false) }
            }
            is InputTransactionEvent.SelectRelatedItem -> {
                _state.update { currentState ->
                    currentState.proposedTransactions.toMutableList().apply {
                        val currentPos = currentState.currentTransactionPosition
                        getOrNull(currentPos)?.let { currentUiState ->
                            this[currentPos] = currentUiState.copy(
                                transaction = currentUiState.transaction.copy(
                                    relatedEntityId = event.kindUuid
                                ),
                                relatedItemKindName = event.kind
                            )
                        }
                    }.let { currentState.copy(proposedTransactions = it) }
                }
                _state.update { it.copy(showRelatedItemDialog = false) }
            }
            InputTransactionEvent.ClearRelatedItemDialog -> {
                _state.update { currentState ->
                    currentState.proposedTransactions.toMutableList().apply {
                        val currentPos = currentState.currentTransactionPosition
                        getOrNull(currentPos)?.let { currentUiState ->
                            this[currentPos] = currentUiState.copy(
                                transaction = currentUiState.transaction.copy(
                                    relatedEntityId = null
                                ),
                                relatedItemKindName = null
                            )
                        }
                    }.let {
                        currentState.copy(proposedTransactions = it)
                    }
                }
            }

            // Date Picker Dialog
            InputTransactionEvent.OpenDatePickerDialog -> {
                _state.update { it.copy(showDatePickerDialog = true) }
            }
            InputTransactionEvent.HideDatePickerDialog -> {
                _state.update { it.copy(showDatePickerDialog = false) }
            }
            is InputTransactionEvent.SelectDate -> {
                _state.update { currentState ->
                    currentState.proposedTransactions.toMutableList().apply {
                        val currentPos = currentState.currentTransactionPosition
                        getOrNull(currentPos)?.let { currentUiState ->
                            this[currentPos] = currentUiState.copy(
                                transaction = currentUiState.transaction.copy(
                                    dateAdded = event.date
                                ))
                        }
                    }.let { currentState.copy(proposedTransactions = it) }
                }
            }
            
            is InputTransactionEvent.ChangeCurrentTransactionPosition -> _state.update { it.copy(currentTransactionPosition = event.position) }

            InputTransactionEvent.SaveTransaction -> viewModelScope.launch {
                state.value.proposedTransactions.forEach { proposedTransaction ->
                    transactionRepository.addTransaction(
                        userId,
                        proposedTransaction.transaction,
                        callback = { savedTransaction, result ->
                            Log.d("SaveTransaction", "Saving transaction (${savedTransaction.uuid}): $result")

                            logDataFlowToFile("TransactionEffect", "Transaction ADDED: ${savedTransaction.uuid} | Result: $result")
                            logDataFlowToFile("TransactionEffect", "full Transaction: $savedTransaction")
                            logDataFlowToFile("TransactionEffect", "")

                            viewModelScope.launch {
                                executeTransactionEffect(savedTransaction, isNeutralizing = false)
                            }
                        }
                    )
                }
            }

            // todo: dangerous bisa ngacak acak sistem
            // Konteks:
            // 1️⃣ Transaksi lama hanya satu: dicari dan dinetralisasi
            // 2️⃣ Transaksi pertama dianggap sebagai pengganti, jadi di-update
            // 3️⃣ Transaksi setelahnya dianggap sebagai penambahan baru
            // 🧠 Catatan:
            // Transaksi yang tersimpan di database awalnya hanya satu.
            // Saat update, yang pertama di-*replace* dan sisanya dianggap tambahan.
            InputTransactionEvent.UpdateTransaction -> viewModelScope.launch {
                state.value.proposedTransactions.forEachIndexed { index, proposedTransaction ->
                    val transaction = proposedTransaction.transaction

                    if (index == 0) {
                        val oldTransaction = firestoreCollectionViewModel.transactions.value.find {
                            it.uuid == transaction.uuid
                        }

                        transactionRepository.updateTransaction(
                            userId,
                            transaction,
                            callback = { result, message ->
                                Log.d("UpdateTransaction", "Updating transaction (${transaction.uuid}): $result")

                                logDataFlowToFile("TransactionEffect", "Transaction UPDATED: ${transaction.uuid} | Result: $result")
                                logDataFlowToFile("TransactionEffect", "full Transaction: $transaction")
                                logDataFlowToFile("TransactionEffect", "")

                                viewModelScope.launch {
                                    if (oldTransaction != null) {
                                        executeTransactionEffect(oldTransaction, isNeutralizing = true)
                                        logDataFlowToFile("TransactionEffect", "Neutralized OLD transaction: ${oldTransaction.uuid}")
                                        logDataFlowToFile("TransactionEffect", "full Old Transaction: $oldTransaction")
                                        logDataFlowToFile("TransactionEffect", "")

                                        executeTransactionEffect(result, isNeutralizing = false)
                                        logDataFlowToFile("TransactionEffect", "Applied Side Effect for New transaction: ${result.uuid}")
                                        logDataFlowToFile("TransactionEffect", "full New Transaction: $result")
                                        logDataFlowToFile("TransactionEffect", "")
                                    } else {
                                        Log.d("UpdateTransaction", "❌ Old transaction not found: ${transaction.uuid}")
                                        logDataFlowToFile("TransactionEffect", "❌ Old transaction not found: ${transaction.uuid}")
                                        logDataFlowToFile("TransactionEffect", "")
                                    }
                                }
                            }
                        )
                    } else {
                        transactionRepository.addTransaction(
                            userId,
                            proposedTransaction.transaction,
                            callback = { savedTransaction, result ->
                                Log.d("SaveTransaction", "Saving transaction (${savedTransaction.uuid}): $result")

                                logDataFlowToFile("TransactionEffect", "Transaction ADDED: ${savedTransaction.uuid} | Result: $result")
                                logDataFlowToFile("TransactionEffect", "full Transaction: $savedTransaction")
                                logDataFlowToFile("TransactionEffect", "")

                                viewModelScope.launch {
                                    executeTransactionEffect(savedTransaction, isNeutralizing = false)
                                }
                            }
                        )
                    }
                }
            }

            InputTransactionEvent.DeleteTransaction -> viewModelScope.launch {
                val firstTransaction = state.value.proposedTransactions.firstOrNull()

                firstTransaction?.let { proposedTransaction ->
                    val targetTransactionUuid = proposedTransaction.transaction.uuid

                    val oldTransaction = firestoreCollectionViewModel.transactions.value.find {
                        it.uuid == targetTransactionUuid
                    }

                    if (oldTransaction != null) {
                        executeTransactionEffect(oldTransaction, isNeutralizing = true)

                        transactionRepository.deleteTransaction(
                            userId,
                            targetTransactionUuid,
                            callback = { result ->
                                Log.d("DeleteTransaction", "Deleting transaction ($targetTransactionUuid): $result")

                                logDataFlowToFile("TransactionEffect", "Transaction DELETED: $targetTransactionUuid | Result: $result")
                                logDataFlowToFile("TransactionEffect", "full Deleted Transaction: $oldTransaction")
                                logDataFlowToFile("TransactionEffect", "")
                            }
                        )
                    } else {
                        Log.d("DeleteTransaction", "❌ Transaction not found: $targetTransactionUuid")
                        logDataFlowToFile("TransactionEffect", "❌ Transaction not found for DELETE: $targetTransactionUuid")
                        logDataFlowToFile("TransactionEffect", "")
                    }
                } ?: logDataFlowToFile("TransactionEffect", "❌ No proposed transaction found for DELETE")
            }
        }
    }

    private suspend fun executeTransactionEffect(transaction: Transaction, isNeutralizing: Boolean = false) {
        val rawAmount = transaction.total
        val transactionTotal = if (isNeutralizing) -rawAmount else rawAmount
        val type = TransactionType.entries[transaction.transactionTypeOrdinal]

        println("${if (isNeutralizing) "Neutralizing" else "Applying"} transaction effect...")
        println("transaction type = $type")

        when (type) {
            TransactionType.EXPENSE, TransactionType.INCOME -> {
                val walletEffect = if (type == TransactionType.EXPENSE) -transactionTotal else transactionTotal
                transaction.relatedEntityId?.let { updateBudgetRealized(it, transactionTotal, transaction.dateAdded, transaction.uuid) }
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, walletEffect) }
            }

            TransactionType.SAVING_DEPOSIT -> {
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, -transactionTotal) }
                transaction.relatedEntityId?.let { updateSavingBalance(it, transactionTotal) }
            }

            TransactionType.SAVING_WITHDRAWAL -> {
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, transactionTotal) }
                transaction.relatedEntityId?.let { updateSavingBalance(it, -transactionTotal) }
            }

            TransactionType.DEBT_PAYMENT -> {
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, -transactionTotal) }
                transaction.relatedEntityId?.let { updateDebtReceivableBalance(it, transactionTotal) }
            }

            TransactionType.RECEIVABLE_PAYMENT -> {
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, transactionTotal) }
                transaction.relatedEntityId?.let { updateDebtReceivableBalance(it, transactionTotal) }
            }

            TransactionType.REGULAR_TRANSFER -> {
                transaction.relatedWalletUuid?.let { updateWalletBalance(it, -transactionTotal) } // from
                transaction.relatedEntityId?.let { updateWalletBalance(it, transactionTotal) }     // to
            }

            else -> println("Unhandled transaction type.")
        }
    }

    private suspend fun updateWalletBalance(walletUuid: String, amountChange: Float) {
        firestoreCollectionViewModel.wallets.value.find { it.uuid == walletUuid }?.let { currentWallet ->
            val before = currentWallet.balance
            val after = before + amountChange
            Log.d("UpdateWallet", "Wallet '${currentWallet.name}' (${currentWallet.uuid}) balance: $before -> $after")

            walletRepository.updateWallet(
                userId,
                currentWallet.copy(balance = after),
                callback = { result ->
                    Log.d("UpdateWallet", "Update result: $result")
                    logDataFlowToFile(
                        "WalletEffect",
                        "Wallet '${currentWallet.name}' balance: $before -> $after | change = $amountChange"
                    )
                    logDataFlowToFile("WalletEffect", "full Wallet: '${currentWallet}'")
                    logDataFlowToFile("WalletEffect", "")
                }
            )
        }
    }

    private suspend fun updateSavingBalance(savingUuid: String, amountChange: Float) {
        firestoreCollectionViewModel.savings.value.find { it.uuid == savingUuid }?.let { currentSaving ->
            val before = currentSaving.collectedAmount
            val after = before + amountChange
            Log.d("UpdateSaving", "Saving '${currentSaving.name}' (${currentSaving.uuid}) collected: $before -> $after")

            savingRepository.updateSaving(
                userId,
                currentSaving.copy(collectedAmount = after),
                callback = { result ->
                    Log.d("UpdateSaving", "Update result: $result")
                    logDataFlowToFile(
                        "SavingEffect",
                        "Saving '${currentSaving.name}' collected: $before -> $after | change = $amountChange"
                    )
                    logDataFlowToFile("SavingEffect", "full Saving: '$currentSaving'")
                    logDataFlowToFile("SavingEffect", "")
                }
            )
        }
    }

    private suspend fun updateDebtReceivableBalance(debtReceivableUuid: String, amountChange: Float) {
        firestoreCollectionViewModel.debtAndReceivables.value.find { it.uuid == debtReceivableUuid }?.let { currentItem ->
            val before = currentItem.paidAmount
            val after = before + amountChange
            Log.d("UpdateDebtReceivable", "${currentItem.name} (${currentItem.uuid}) paid amount: $before -> $after")

            debtReceivableRepository.updateDebtAndReceivable(
                userId,
                currentItem.copy(paidAmount = after),
                callback = { result ->
                    Log.d("UpdateDebtReceivable", "Update result: $result")
                    logDataFlowToFile(
                        "DebtReceivableEffect",
                        "DebtReceivable '${currentItem.name}' paid amount: $before -> $after | change = $amountChange"
                    )
                    logDataFlowToFile("DebtReceivableEffect", "full DebtReceivable: '$currentItem'")
                    logDataFlowToFile("DebtReceivableEffect", "")
                }
            )
        }
    }

    private suspend fun updateBudgetRealized(
        categoryUuid: String,
        transactionAmount: Float,
        transactionDate: Timestamp,
        transactionUuid: String
    ) {
        firestoreCollectionViewModel.budgets.value.find { budget ->
            budget.involvedCategoriesUuid?.contains(categoryUuid) == true
        }?.let { budget ->
            Log.d("UpdateBudget", "Found budget '${budget.name}' (${budget.uuid}) for category: $categoryUuid")

            val updatedRealizations = budget.budgetRealizations.mapIndexed { index, realization ->
                if (!realization.isDeleted &&
                    transactionDate.seconds in realization.startTime.seconds..realization.endTime.seconds
                ) {
                    val before = realization.progressAmount
                    val after = before + transactionAmount

                    Log.d("UpdateBudget", "Realization at index $index [${realization.startTime} - ${realization.endTime}] progress: $before -> $after")

                    logDataFlowToFile(
                        "BudgetEffect",
                        "Budget '${budget.name}' realization index $index: progress $before -> $after | amount = $transactionAmount | txnUuid = $transactionUuid"
                    )
                    logDataFlowToFile("BudgetEffect", "full Budget: '$budget'")
                    logDataFlowToFile("BudgetEffect", "")

                    realization.copy(
                        progressAmount = after,
                        involvedTransactionsUuid = (realization.involvedTransactionsUuid.orEmpty() + transactionUuid).distinct()
                    )
                } else {
                    realization
                }
            }

            budgetRepository.updateBudget(
                userId,
                budget.copy(budgetRealizations = updatedRealizations),
                callback = { result ->
                    Log.d("UpdateBudget", "Update result for budget ${budget.name} (${budget.uuid}): $result")
                    logDataFlowToFile("BudgetEffect", "Update result for budget ${budget.name}: $result")
                    logDataFlowToFile("BudgetEffect", "")
                }
            )
        } ?: Log.d("UpdateBudget", "❌ No budget found for category $categoryUuid at date $transactionDate")
    }
}