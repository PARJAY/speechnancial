package com.example.speechnancial.common

import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.EnumTransactionType
import com.example.speechnancial.data.firebase.model.Transaction
import com.google.firebase.Timestamp
import java.util.Calendar

/* firebase */
// Constants (Put these in a separate file or object if you prefer)
const val USER_COLLECTION = "users"
const val TRANSACTION_COLLECTION = "transactions"
const val TRANSACTION_DETAIL_COLLECTION = "transaction-"
const val CATEGORY_COLLECTION = "categories"
const val BUDGET_COLLECTION = "budgets"
const val WALLETS_COLLECTION = "wallets"
const val KEYWORDS_COLLECTION = "keywords"
const val INTERNET_ISSUE = "Please check your internet connection"

// Transaction Related Start
val nominalUnits = mapOf(
    "puluh" to 10.0,
    "ratus" to 100.0,
    "ribu" to 1_000.0,
    "juta" to 1_000_000.0,
    "miliar" to 1_000_000_000.0,
    "triliun" to 1_000_000_000_000.0
)

val pengeluaranKeywords =
    setOf(
        "pengeluaran",
        "beli",
        "bayar",
        "belanja",
        "transportasi",
        "tagihan",
        "sewa",
        "listrik",
        "internet",
        "air",
        "pendidikan",
        "makan",
        "minum",
        "perawatan",
        "kesehatan",
        "hiburan",
        "cicilan",
        "donasi",
        "asuransi",
        "pajak",
        "hobi",
        "liburan",
        "biaya administrasi"
    )

val pemasukanKeywords =
    setOf(
        "pemasukan",
        "gaji",
        "bonus",
        "hadiah",
        "laba",
        "pengembalian",
        "investasi",
        "bunga",
        "deposito",
        "penjualan",
        "komisi",
        "royalti",
        "sumbangan",
        "uang kaget",
        "thr",
        "refund",
        "insentif",
        "hibah",
        "uang saku",
        "penghargaan",
        "dividen"
    )



val transaction1 = Transaction(
    uuid = "transaction123",
    fullText = "Pembelian Bahan Makanan",
    details = mapOf("Beras" to 50000.0f, "Telur" to 30000.0f, "Sayuran" to 20000.0f),
    total = 100000.0f,
    transactionTypeOldOrdinalOld = EnumTransactionType.OUTCOME.ordinal,
//    categoryUuid = "categoryFood",
//    relatedWalletFromUuid = "walletABC",
    isValid = true
)

val transaction2 = Transaction(
    uuid = "transaction456",
    fullText = "Gaji Bulanan",
    details = mapOf("Gaji Pokok" to 5000000.0f, "Bonus" to 500000.0f),
    total = 5500000.0f,
    transactionTypeOldOrdinalOld = EnumTransactionType.INCOME.ordinal,
//    categoryUuid = "categorySalary",
//    relatedWalletToUuid = "walletXYZ",
    isValid = true
)

val transaction3 = Transaction(
    uuid = "transaction789",
    fullText = "Transfer dari Dompet A ke Dompet B",
    total = 1000000.0f,
    transactionTypeOldOrdinalOld = EnumTransactionType.TRANSFER.ordinal,
//    relatedWalletFromUuid = "walletA",
//    relatedWalletToUuid = "walletB",
    isValid = true
)

val transaction4 = Transaction(
    uuid = "transaction101",
    fullText = "Pembayaran Tagihan Listrik",
    total = 250000.0f,
    transactionTypeOldOrdinalOld = EnumTransactionType.OUTCOME.ordinal,
//    categoryUuid = "categoryUtilities",
//    relatedWalletFromUuid = "walletABC",
    isNeedRevise = true
)

val transaction5 = Transaction(
    uuid = "transaction112",
    fullText = "Pembelian dari Smartwatch",
    details = mapOf("Coffee" to 25000.0f, "Snacks" to 15000.0f),
    total = 40000.0f,
    transactionTypeOldOrdinalOld = EnumTransactionType.OUTCOME.ordinal,
//    categoryUuid = "categoryFood",
//    relatedWalletFromUuid = "walletABC",
    isFromSmartwatch = true,
    isValid = true
)

// Transaction Related End



// Categories Related Start

// Categories Related End


// Budgeting Related Start

val calendar = Calendar.getInstance()

// 1. Belanja Harian
val budgetExample1BelanjaHarian = Budget(
    uuid = "budget-1",
    name = "Belanja Harian",
    amount = 50000.0f,
    startTime = Timestamp.now(),
    timeRangeInDays = 1,
    recurringTypeOrdinal = EnumTimeRange.DAILY.ordinal,
    transactionTypeOrdinal = EnumTransactionType.OUTCOME.ordinal,
    budgetRealizations = listOf(
        BudgetRealization(
            progressAmount = 65000.0f,
            startTime = Timestamp.now(),
            endTime = Timestamp(calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }.time)
        ),
        BudgetRealization(
            progressAmount = 10000.0f,
            startTime = Timestamp(calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }.time),
            endTime = Timestamp(calendar.apply { add(Calendar.DAY_OF_MONTH, 2) }.time)
        ),
        BudgetRealization(
            progressAmount = 0f,
            startTime = Timestamp(calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }.time),
            endTime = Timestamp(calendar.apply { add(Calendar.DAY_OF_MONTH, 2) }.time)
        )
    ),
    // how to declare a document reference
    involvedCategoriesUuid = listOf(
        "category1UUid", "category2UUid"
    )
)

// 2. Gajian Karyawan Magang
val budgetExample2GajianMagang = Budget(
    uuid = "budget-2",
    name = "Gajian Karyawan Magang",
    amount = 1500000.0f,
    startTime = Timestamp.now(),
    timeRangeInDays = 30,
    recurringTypeOrdinal = EnumTimeRange.MONTHLY.ordinal,
    transactionTypeOrdinal = EnumTransactionType.INCOME.ordinal,
    budgetRealizations = listOf(
        BudgetRealization(
            progressAmount = 1500000.0f,
            startTime = Timestamp.now(),
            endTime = Timestamp(calendar.apply { add(Calendar.MONTH, 1) }.time)
        ),
        BudgetRealization(
            progressAmount = 2000000.0f,
            startTime = Timestamp(calendar.apply { add(Calendar.MONTH, 1) }.time),
            endTime = Timestamp(calendar.apply { add(Calendar.MONTH, 2) }.time)
        )
    )
)

// 3. Transportasi
val budgetExample3Transportasi = Budget(
    uuid = "budget-3",
    name = "Transportasi",
    amount = 200000.0f,
    startTime = Timestamp.now(),
    timeRangeInDays = 7,
    recurringTypeOrdinal = EnumTimeRange.WEEKLY.ordinal,
    transactionTypeOrdinal = EnumTransactionType.OUTCOME.ordinal,
    budgetRealizations = listOf(
        BudgetRealization(
            progressAmount = 100000.0f,
            startTime = Timestamp.now(),
            endTime = Timestamp(calendar.apply { add(Calendar.WEEK_OF_MONTH, 1) }.time)
        ),
        BudgetRealization(
            progressAmount = 50000.0f,
            startTime = Timestamp(calendar.apply { add(Calendar.WEEK_OF_MONTH, 1) }.time),
            endTime = Timestamp(calendar.apply { add(Calendar.WEEK_OF_MONTH, 2) }.time)
        )
    )
)

// 4. THR
val budgetExample4Thr = Budget(
    uuid = "budget-4",
    name = "THR",
    amount = 3000000.0f,
    startTime = Timestamp.now(),
    timeRangeInDays = 30,
    recurringTypeOrdinal = EnumTimeRange.NOT_RECURRING.ordinal,
    transactionTypeOrdinal = EnumTransactionType.INCOME.ordinal,
    budgetRealizations = listOf(
        BudgetRealization(
            progressAmount = 4000000.0f,
            startTime = Timestamp.now(),
            endTime = Timestamp(calendar.apply { add(Calendar.MONTH, 1) }.time)
        ),
        BudgetRealization(
            progressAmount = 0.0f,
            startTime = Timestamp(calendar.apply { add(Calendar.MONTH, 1) }.time),
            endTime = Timestamp(calendar.apply { add(Calendar.MONTH, 2) }.time)
        )
    )
)

// Budgeting Related End