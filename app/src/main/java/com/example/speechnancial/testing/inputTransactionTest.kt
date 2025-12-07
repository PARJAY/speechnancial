package com.example.speechnancial.testing

import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.Category
import com.example.speechnancial.data.firebase.model.DebtAndReceivable
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.data.firebase.model.Saving
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.data.firebase.repository.ITransactionRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.random.Random

// dummy data storage ada di bagian luar agar data konsisten
private val wallets = mutableMapOf(
    "wallet-001" to Wallet(uuid = "wallet-001", name = "Main Wallet", balance = 500_000f),
    "wallet-002" to Wallet(uuid = "wallet-002", name = "Backup Wallet", balance = 300_000f)
)

private val categories = mutableMapOf(
    "category-food" to Category(
        uuid = "category-food",
        name = "Food",
        enumTransactionType = EnumTransactionType.OUTCOME.ordinal
    ),
    "category-salary" to Category(
        uuid = "category-salary",
        name = "Salary",
        enumTransactionType = EnumTransactionType.INCOME.ordinal
    )
)

private fun createRealizationPeriod(daysFromNow: Int = 7): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.time = Date() // sekarang
    calendar.add(Calendar.DAY_OF_YEAR, daysFromNow)
    return Timestamp(calendar.time)
}

private val realizationStart = createRealizationPeriod(-1)
private val realizationEnd = createRealizationPeriod()

private val budgetRealizationOutcome = BudgetRealization(
    uuid = "realization-001",
    progressAmount = 0f,
    startTime = realizationStart,
    endTime = realizationEnd,
    involvedTransactionsUuid = mutableListOf()
)

private val budgetRealizationIncome = BudgetRealization(
    uuid = "realization-002",
    progressAmount = 0f,
    startTime = realizationStart,
    endTime = realizationEnd,
    involvedTransactionsUuid = mutableListOf()
)

private val budgets = mutableMapOf(
    "budget-001" to Budget(
        uuid = "budget-001",
        name = "Food Budget",
        amount = 1_000_000f,
        involvedCategoriesUuid = listOf("category-food"),
        budgetRealizations = listOf(budgetRealizationOutcome),
        transactionTypeOrdinal = EnumTransactionType.OUTCOME.ordinal
    ),
    "budget-002" to Budget(
        uuid = "budget-002",
        name = "Salary Budget",
        amount = 2_000_000f,
        involvedCategoriesUuid = listOf("category-salary"),
        budgetRealizations = listOf(budgetRealizationIncome),
        transactionTypeOrdinal = EnumTransactionType.INCOME.ordinal
    )
)


private val savings = mutableMapOf(
    "saving-001" to Saving(uuid = "saving-001", name = "Vacation", collectedAmount = 300_000f)
)

private val debts = mutableMapOf(
    "debt-001" to DebtAndReceivable(uuid = "debt-001", name = "Loan", amount = 1_000_000f, paidAmount = 200_000f),
    "recv-001" to DebtAndReceivable(uuid = "recv-001", name = "Receivable from John", amount = 500_000f, paidAmount = 100_000f)
)

// Transactions
val transactions = listOf(
    Transaction(
        uuid = "tx-expense",
        total = 150_000f,
        transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "category-food",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-income",
        total = 200_000f,
        transactionTypeOrdinal = TransactionType.INCOME.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "category-salary",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-transfer",
        total = 100_000f,
        transactionTypeOrdinal = TransactionType.REGULAR_TRANSFER.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "wallet-002",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-saving-deposit",
        total = 50_000f,
        transactionTypeOrdinal = TransactionType.SAVING_DEPOSIT.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "saving-001",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-saving-withdraw",
        total = 25_000f,
        transactionTypeOrdinal = TransactionType.SAVING_WITHDRAWAL.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "saving-001",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-debt-payment",
        total = 300_000f,
        transactionTypeOrdinal = TransactionType.DEBT_PAYMENT.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "debt-001",
        dateAdded = Timestamp.now()
    ),
    Transaction(
        uuid = "tx-recv-payment",
        total = 150_000f,
        transactionTypeOrdinal = TransactionType.RECEIVABLE_PAYMENT.ordinal,
        relatedWalletUuid = "wallet-001",
        relatedEntityId = "recv-001",
        dateAdded = Timestamp.now()
    )
)

fun main() {
    val fakeRepo = FakeTransactionRepository()
    val fakeViewModel = TestTransactionProcessor(fakeRepo)

            // Jalankan semua transaksi
    runBlocking {
        for (tx in transactions) {
            fakeViewModel.processTransaction(tx, isUpdate = true)
            delay(300)
            println("")
            println("__________________________________________")
            println("")
        }
    }
}

class FakeTransactionRepository : ITransactionRepository {
    override suspend fun addTransaction(
        userId: String,
        transaction: Transaction,
        callback: (Transaction, String) -> Unit
    ) {
        // todo: this task will never be done
    }

    override suspend fun updateTransaction(
        userId: String,
        transaction: Transaction,
        callback: (Transaction, String) -> Unit
    ) {
        // todo: this task will never be done
    }

    override suspend fun deleteTransaction(
        userId: String,
        transactionId: String,
        callback: (String) -> Unit
    ) {
        // todo: this task will never be done
    }
}

class TestTransactionProcessor(
    private val transactionRepository: ITransactionRepository
) {
    suspend fun processTransaction(
        transaction: Transaction,
        isUpdate: Boolean = false,
        originalTransaction: Transaction? = transactions[Random.nextInt(transactions.size - 1)],
        isDelete: Boolean = false
    ) {
        when {
            isDelete -> {
//                transactionRepository.deleteTransaction(transaction.uuid) {
//                    println("Callback delete result: $it")
//                }
                println("Neutralizing deleted transaction...")
                printTransactionEffect(transaction, isNeutralizing = true)
            }

            isUpdate && originalTransaction != null -> {
//                transactionRepository.updateTransaction(transaction) {
//                    println("Callback update result: $it")
//                }
                println("Neutralizing original transaction...")
                printTransactionEffect(originalTransaction, isNeutralizing = true)

                println("")
                println("")

                println("Applying updated transaction...")
                printTransactionEffect(transaction)
            }

            else -> {
//                transactionRepository.addTransaction(transaction) {
//                    println("Callback save result: $it")
//                }
                printTransactionEffect(transaction)
            }
        }
    }
}

fun findMatchingRealization(categoryUuid: String, dateAdded: Timestamp): BudgetRealization? {
    for ((_, budget) in budgets) {
        val involvedCategories = budget.involvedCategoriesUuid.orEmpty()

        if (categoryUuid !in involvedCategories) {
            continue
        }

        val matched = budget.budgetRealizations.firstOrNull {
            dateAdded >= it.startTime && dateAdded <= it.endTime && !it.isDeleted
        }

        if (matched != null) {
            return matched
        }
    }

    return null
}

fun findMatchingWithDebugRealization(categoryUuid: String, dateAdded: Timestamp): BudgetRealization? {
    println("Mencari realization untuk kategori: $categoryUuid pada tanggal: $dateAdded")

    for ((_, budget) in budgets) {
        println("🔍 Memeriksa budget: ${budget.uuid} - ${budget.name}")
        val involvedCategories = budget.involvedCategoriesUuid.orEmpty()
        println("Kategori dalam budget: $involvedCategories")

        if (categoryUuid !in involvedCategories) {
            println("⛔ Kategori tidak cocok dengan budget ini.")
            continue
        }

        println("📅 Realisasi yang tersedia:")
        budget.budgetRealizations.forEach {
            println("- ${it.uuid}: ${it.startTime} s.d. ${it.endTime} | deleted: ${it.isDeleted}")
            println("  > Cocok? Start <= dateAdded: ${dateAdded >= it.startTime}, End >= dateAdded: ${dateAdded <= it.endTime}")
        }

        val matched = budget.budgetRealizations.firstOrNull {
            dateAdded >= it.startTime && dateAdded <= it.endTime && !it.isDeleted
        }

        if (matched != null) {
            println("✅ Realisasi cocok ditemukan: ${matched.uuid}")
            return matched
        } else {
            println("❌ Tidak ada Realisasi cocok dalam budget ini.")
        }
    }

    println("🚫 Tidak ditemukan budget mana pun yang cocok dengan kategori.")
    return null
}

fun printTransactionEffect(transaction: Transaction, isNeutralizing: Boolean = false) {
    val rawAmount = transaction.total
    val transactionTotal = if (isNeutralizing) -rawAmount else rawAmount
    val fromWallet = wallets[transaction.relatedWalletUuid]
    val relatedId = transaction.relatedEntityId

    val sign = if (isNeutralizing) "Neutralizing" else "Applying"
    println("$sign transaction effect...")

    println("transaction type = ${TransactionType.entries[transaction.transactionTypeOrdinal]}")
    when (TransactionType.entries[transaction.transactionTypeOrdinal]) {
        TransactionType.EXPENSE -> {
            val categoryUuid = transaction.relatedEntityId ?: ""
            val realization = findMatchingWithDebugRealization(categoryUuid, transaction.dateAdded)

            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.minus(transactionTotal)}") // should be minus not plus

            if (realization != null) {
                println("Budget Realized: ${realization.progressAmount} -> ${realization.progressAmount + transactionTotal}")
            } else {
                println("No matching budget realization found.")
            }
        }

        TransactionType.INCOME -> {
            val categoryUuid = transaction.relatedEntityId ?: ""
            val realization = findMatchingWithDebugRealization(categoryUuid, transaction.dateAdded)

            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.plus(transactionTotal)}")

            if (realization != null) {
                println("Budget Realized: ${realization.progressAmount} -> ${realization.progressAmount + transactionTotal}")
            } else {
                println("No matching budget realization found.")
            }
        }

        TransactionType.SAVING_DEPOSIT -> {
            val saving = savings[relatedId]
            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.minus(transactionTotal)} from target ${saving?.targetAmount}")
            println("Saving Balance: ${saving?.collectedAmount} -> ${saving?.collectedAmount?.plus(transactionTotal)} from target ${saving?.targetAmount}")
        }

        TransactionType.SAVING_WITHDRAWAL -> {
            val saving = savings[relatedId]
            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.plus(transactionTotal)} from target ${saving?.targetAmount}")
            println("Saving Balance: ${saving?.collectedAmount} -> ${saving?.collectedAmount?.minus(transactionTotal)} from target ${saving?.targetAmount}")
        }

        TransactionType.DEBT_PAYMENT -> {
            val debt = debts[relatedId]
            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.minus(transactionTotal)} from total ${debt?.amount}")
            println("Debt Paid: ${debt?.paidAmount} -> ${debt?.paidAmount?.plus(transactionTotal)}  from total ${debt?.amount}")
        }

        TransactionType.RECEIVABLE_PAYMENT -> {
            val debt = debts[relatedId]
            println("Transaction Total: $transactionTotal")
            println("Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.plus(transactionTotal)} from total ${debt?.amount}")
            println("Receivable Paid: ${debt?.paidAmount} -> ${debt?.paidAmount?.plus(transactionTotal)} from total ${debt?.amount}")
        }

        TransactionType.REGULAR_TRANSFER -> {
            val toWallet = wallets[relatedId]
            println("Transaction Total: $transactionTotal")
            println("From Wallet Balance: ${fromWallet?.balance} -> ${fromWallet?.balance?.minus(transactionTotal)}")
            println("To Wallet Balance: ${toWallet?.balance} -> ${toWallet?.balance?.plus(transactionTotal)}")
        }

        else -> println("Unhandled transaction type: ${transaction.transactionTypeOrdinal}")
    }
}