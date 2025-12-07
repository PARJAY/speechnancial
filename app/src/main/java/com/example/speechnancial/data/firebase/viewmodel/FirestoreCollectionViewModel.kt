package com.example.speechnancial.data.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speechnancial.MyApp
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Keyword
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.tools.Util.Companion.logDataFlowToFile
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class FirestoreCollectionViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets.asStateFlow()

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _savings = MutableStateFlow<List<Saving>>(emptyList())
    val savings: StateFlow<List<Saving>> = _savings.asStateFlow()

    private val _keywords = MutableStateFlow<List<Keyword>>(emptyList())
    val keywords: StateFlow<List<Keyword>> = _keywords.asStateFlow()

    private val _debtAndReceivables = MutableStateFlow<List<DebtAndReceivable>>(emptyList())
    val debtAndReceivables: StateFlow<List<DebtAndReceivable>> = _debtAndReceivables.asStateFlow()

    // Shared variable untuk menyimpan transaksi yang dipilih untuk diedit
    private val _selectedTransaction = MutableStateFlow(Transaction())
    val selectedTransaction: StateFlow<Transaction> = _selectedTransaction.asStateFlow()

    // Fungsi untuk menetapkan transaksi yang dipilih
    fun setSelectedTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }

    private var userId: String = ""

    fun initUserData(uid: String) {
        if (userId.isEmpty()) {
            userId = uid
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            getTransactionListFromRepo()
            getCategoryListFromRepo()
            getBudgetListFromRepo()
            getWalletListFromRepo()
            getSavingListFromRepo()
            getDebtAndReceivableListFromRepo()
        }
    }

    private fun getSavingListFromRepo() {
        MyApp.appModule.savingRepositoryImpl.getSavingList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("SavingFCVM", "❌ ERROR: ${exception.message}")
            },
            addDataCallback = { saving ->
                logDataFlowToFile("SavingFCVM", "➕ ADD: ${saving.uuid}")
                logDataFlowToFile("SavingFCVM", "full: $saving")
                logDataFlowToFile("SavingFCVM", "")
                _savings.value += saving
            },
            updateDataCallback = { saving ->
                _savings.value = _savings.value.map {
                    if (it.uuid == saving.uuid) {
                        logDataFlowToFile("SavingFCVM", "✏️ UPDATE: ${saving.uuid}")
                        logDataFlowToFile("SavingFCVM", "from: $it")
                        logDataFlowToFile("SavingFCVM", "to:   $saving")
                        logDataFlowToFile("SavingFCVM", "")
                        saving
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("SavingFCVM", "🗑️ DELETE: $documentId")
                logDataFlowToFile("SavingFCVM", "")
                _savings.value = _savings.value.filter { it.uuid != documentId }
            }
        )
    }

    private fun getDebtAndReceivableListFromRepo() {
        MyApp.appModule.debtAndReceivableRepositoryImpl.getDebtAndReceivableList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("DebtReceivableFCVM", "ERROR: ${exception.message}")
            },
            addDataCallback = { debtAndReceivable ->
                logDataFlowToFile("DebtReceivableFCVM", "ADD: $debtAndReceivable")
                _debtAndReceivables.value += debtAndReceivable
            },
            updateDataCallback = { debtAndReceivable ->
                _debtAndReceivables.value = _debtAndReceivables.value.map {
                    if (it.uuid == debtAndReceivable.uuid) {
                        logDataFlowToFile("DebtReceivableFCVM", "UPDATE: from $it to $debtAndReceivable")
                        debtAndReceivable
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("DebtReceivableFCVM", "DELETE: $documentId")
                _debtAndReceivables.value = _debtAndReceivables.value.filter { it.uuid != documentId }
            }
        )
    }

    private fun getCategoryListFromRepo() {
        MyApp.appModule.categoryRepositoryImpl.getCategoryList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("CategoryFCVM", "❌ ERROR: ${exception.message}")
            },
            addDataCallback = { category ->
                logDataFlowToFile("CategoryFCVM", "➕ ADD: ${category.uuid}")
                logDataFlowToFile("CategoryFCVM", "full: $category")
                logDataFlowToFile("CategoryFCVM", "")
                _categories.value += category
            },
            updateDataCallback = { category ->
                _categories.value = _categories.value.map {
                    if (it.uuid == category.uuid) {
                        logDataFlowToFile("CategoryFCVM", "✏️ UPDATE: ${category.uuid}")
                        logDataFlowToFile("CategoryFCVM", "from: $it")
                        logDataFlowToFile("CategoryFCVM", "to:   $category")
                        logDataFlowToFile("CategoryFCVM", "")
                        category
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("CategoryFCVM", "🗑️ DELETE: $documentId")
                logDataFlowToFile("CategoryFCVM", "")
                _categories.value = _categories.value.filter { it.uuid != documentId }
            }
        )
    }

    private fun getBudgetListFromRepo() {
        MyApp.appModule.budgetRepositoryImpl.getBudgetList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("BudgetFCVM", "❌ ERROR: ${exception.message}")
            },
            addDataCallback = { budget ->
                populateBudgetRealizations(budget)
                logDataFlowToFile("BudgetFCVM", "➕ ADD: ${budget.uuid}")
                logDataFlowToFile("BudgetFCVM", "full: $budget")
                logDataFlowToFile("BudgetFCVM", "")
                _budgets.value += budget
            },
            updateDataCallback = { budget ->
                _budgets.value = _budgets.value.map {
                    if (it.uuid == budget.uuid) {
                        logDataFlowToFile("BudgetFCVM", "✏️ UPDATE: ${budget.uuid}")
                        logDataFlowToFile("BudgetFCVM", "from: $it")
                        logDataFlowToFile("BudgetFCVM", "to:   $budget")
                        logDataFlowToFile("BudgetFCVM", "")
                        budget
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("BudgetFCVM", "🗑️ DELETE: $documentId")
                logDataFlowToFile("BudgetFCVM", "")
                _budgets.value = _budgets.value.filter { it.uuid != documentId }
            }
        )
    }

    private fun populateBudgetRealizations(budget: Budget) {
        val now = Timestamp.now()
        val systemZoneId = ZoneId.systemDefault()
        val zoneOffset = systemZoneId.rules.getOffset(Instant.now())
        val today = now.toDate().toInstant().atZone(systemZoneId).toLocalDate()

        if (budget.recurringTypeOrdinal == EnumTimeRange.NOT_RECURRING.ordinal) {
            return
        }

        // Konversi startTime ke LocalDate
        var currentRealizationDate = budget.startTime.toDate().toInstant().atZone(systemZoneId).toLocalDate()
        val expectedRealizations = mutableListOf<BudgetRealization>()

        // cek data terakhir budgetRealization
        if (budget.budgetRealizations.isNotEmpty()) {
            val lastRealizationStartDateTime = LocalDateTime.ofEpochSecond(
                budget.budgetRealizations.last().startTime.seconds,
                budget.budgetRealizations.last().startTime.nanoseconds,
                zoneOffset
            )
            val lastRealizationEndDateTime = LocalDateTime.ofEpochSecond(
                budget.budgetRealizations.last().endTime.seconds,
                budget.budgetRealizations.last().endTime.nanoseconds,
                zoneOffset
            )
            val lastRealizationStartDate = lastRealizationStartDateTime.toLocalDate()
            val lastRealizationEndDate = lastRealizationEndDateTime.toLocalDate()

            if (today in lastRealizationStartDate..lastRealizationEndDate) {
                logDataFlowToFile("BudgetFCVM", "populateBudgetRealizations: Already up-to-date for ${budget.uuid}")
                return
            }
        }

        while (currentRealizationDate <= today) {
            val realizationStartTimeMillis = currentRealizationDate
                .atStartOfDay(systemZoneId)
                .toInstant()
                .toEpochMilli()

            val nextStartDate = when (budget.recurringTypeOrdinal) {
                EnumTimeRange.DAILY.ordinal -> currentRealizationDate.plusDays(1)
                EnumTimeRange.WEEKLY.ordinal -> currentRealizationDate.plusWeeks(1)
                EnumTimeRange.MONTHLY.ordinal -> currentRealizationDate.plusMonths(1)
                EnumTimeRange.YEARLY.ordinal -> currentRealizationDate.plusYears(1)
                EnumTimeRange.CUSTOM.ordinal -> currentRealizationDate.plusDays(budget.timeRangeInDays.toLong())
                else -> currentRealizationDate.plusDays(1)
            }

            val realizationEndTimeMillis = nextStartDate
                .atStartOfDay(systemZoneId)
                .minusNanos(1) // 1 nanodetik sebelum hari berikutnya
                .toInstant()
                .toEpochMilli()

            val realization = BudgetRealization(
                progressAmount = 0f,
                startTime = Timestamp(realizationStartTimeMillis / 1000, (realizationStartTimeMillis % 1000).toInt() * 1_000_000),
                endTime = Timestamp(realizationEndTimeMillis / 1000, (realizationEndTimeMillis % 1000).toInt() * 1_000_000),
                involvedTransactionsUuid = null,
                isDeleted = false
            )
            expectedRealizations.add(realization)

            currentRealizationDate = nextStartDate
        }

        val existingRealizationsMap = budget.budgetRealizations.associateBy {
            LocalDateTime.ofEpochSecond(it.startTime.seconds, it.startTime.nanoseconds, zoneOffset).toLocalDate()
        }
        val mergedRealizations = expectedRealizations.map { expectedRealization ->
            existingRealizationsMap[LocalDateTime.ofEpochSecond(expectedRealization.startTime.seconds, expectedRealization.startTime.nanoseconds, zoneOffset).toLocalDate()] ?: expectedRealization
        }

        viewModelScope.launch {
            logDataFlowToFile("BudgetFCVM", "populateBudgetRealizations: BEFORE update -> ${budget.budgetRealizations}")
            logDataFlowToFile("BudgetFCVM", "populateBudgetRealizations: AFTER update -> $mergedRealizations")

            MyApp.appModule.budgetRepositoryImpl.updateBudget(userId, budget.copy(budgetRealizations = mergedRealizations)) { message ->
                logDataFlowToFile("BudgetFCVM", "populateBudgetRealizations: Network call response -> $message")
            }
        }
    }

    private fun getWalletListFromRepo() {
        MyApp.appModule.walletRepositoryImpl.getWalletList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("WalletFCVM", "❌ ERROR: ${exception.message}")
            },
            addDataCallback = { wallet ->
                logDataFlowToFile("WalletFCVM", "➕ ADD: ${wallet.uuid}")
                logDataFlowToFile("WalletFCVM", "full: $wallet")
                logDataFlowToFile("WalletFCVM", "")
                _wallets.value += wallet
            },
            updateDataCallback = { wallet ->
                _wallets.value = _wallets.value.map {
                    if (it.uuid == wallet.uuid) {
                        logDataFlowToFile("WalletFCVM", "✏️ UPDATE: ${wallet.uuid}")
                        logDataFlowToFile("WalletFCVM", "from: $it")
                        logDataFlowToFile("WalletFCVM", "to:   $wallet")
                        logDataFlowToFile("WalletFCVM", "")
                        wallet
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("WalletFCVM", "🗑️ DELETE: $documentId")
                logDataFlowToFile("WalletFCVM", "")
                _wallets.value = _wallets.value.filter { it.uuid != documentId }
            }
        )
    }

    private fun getTransactionListFromRepo() {
        MyApp.appModule.transactionRepositoryImpl.getTransactionList(
            userId,
            errorCallback = { exception ->
                logDataFlowToFile("TransactionFCVM", "❌ ERROR: ${exception.message}")
            },
            addDataCallback = { transaction ->
                logDataFlowToFile("TransactionFCVM", "➕ ADD: ${transaction.uuid}")
                logDataFlowToFile("TransactionFCVM", "full: $transaction")
                logDataFlowToFile("TransactionFCVM", "")
                _transactions.value += transaction
                _transactions.value = _transactions.value.sortedByDescending { it.dateAdded }
            },
            updateDataCallback = { transaction ->
                _transactions.value = _transactions.value.map {
                    if (it.uuid == transaction.uuid) {
                        logDataFlowToFile("TransactionFCVM", "✏️ UPDATE: ${transaction.uuid}")
                        logDataFlowToFile("TransactionFCVM", "from: $it")
                        logDataFlowToFile("TransactionFCVM", "to:   $transaction")
                        logDataFlowToFile("TransactionFCVM", "")
                        transaction
                    } else it
                }
            },
            deleteDataCallback = { documentId ->
                logDataFlowToFile("TransactionFCVM", "🗑️ DELETE: $documentId")
                logDataFlowToFile("TransactionFCVM", "")
                _transactions.value = _transactions.value.filter { it.uuid != documentId }
            }
        )
    }
}