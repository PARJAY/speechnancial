package com.example.speechnancial.tools

import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.tools.Util.Companion.createOrUpdateBudgetRealizationsWithPrint
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun main() {
    println("=== DAILY ===")
    dailyBudgetRealization()

    println("\n=== WEEKLY ===")
    weeklyBudgetRealization()

    println("\n=== MONTHLY ===")
    monthlyBudgetRealization()

    println("\n=== YEARLY ===")
    yearlyBudgetRealization()
}


fun dailyBudgetRealization() {
    val zoneId = ZoneId.systemDefault()

    // Buat budget yang dimulai 5 hari lalu
    val startDate = LocalDate.now().minusDays(5)
    val startTimestamp = Timestamp(Date.from(startDate.atStartOfDay(zoneId).toInstant()))

    val budget = Budget(
        uuid = "budget-1",
        name = "Makanan",
        amount = 500_000f,
        startTime = startTimestamp,
        timeRangeInDays = 3, // hanya digunakan jika recurringTypeOrdinal == CUSTOM
        recurringTypeOrdinal = EnumTimeRange.DAILY.ordinal, // Ganti ini ke MONTHLY, YEARLY, dll untuk pengujian lain
        involvedCategoriesUuid = listOf("cat-1", "cat-2")
    )

    // Transaksi dalam beberapa hari terakhir, beberapa relevan dan beberapa tidak
    val transactions = listOf(
        Transaction(
            uuid = "trx-1",
            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
            relatedEntityId = "cat-1",
            dateAdded = Timestamp(Date.from(LocalDate.now().minusDays(1).atStartOfDay(zoneId).toInstant()))
        ),
        Transaction(
            uuid = "trx-2",
            transactionTypeOrdinal = TransactionType.INCOME.ordinal,
            relatedEntityId = "cat-3", // Tidak relevan (category tidak cocok)
            dateAdded = Timestamp(Date.from(LocalDate.now().atStartOfDay(zoneId).toInstant()))
        ),
        Transaction(
            uuid = "trx-3",
            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
            relatedEntityId = "cat-2",
            dateAdded = Timestamp(Date.from(LocalDate.now().atStartOfDay(zoneId).toInstant()))
        )
    )

    // Jalankan realisasi budget
    val result = createOrUpdateBudgetRealizationsWithPrint(budget, transactions)

    // Cetak hasil akhirnya
    println("\nFinal budget realizations:")
    result.forEachIndexed { i, br ->
//        println("[$i] Start: ${br.startTime.toDate()}, End: ${br.endTime.toDate()}, Transactions: ${br.involvedTransactionsUuid}")
        println(br)
    }
}

fun weeklyBudgetRealization() {
    val zoneId = ZoneId.systemDefault()
    val startDate = LocalDate.now().minusWeeks(2)
    val startTimestamp = Timestamp(Date.from(startDate.atStartOfDay(zoneId).toInstant()))

    val budget = Budget(
        uuid = "budget-2",
        name = "Transportasi",
        amount = 200_000f,
        startTime = startTimestamp,
        timeRangeInDays = 7,
        recurringTypeOrdinal = EnumTimeRange.WEEKLY.ordinal,
        involvedCategoriesUuid = listOf("cat-4", "cat-5")
    )

    val transactions = listOf(
        Transaction(
            uuid = "trx-4",
            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
            relatedEntityId = "cat-4",
            dateAdded = Timestamp(Date.from(LocalDate.now().minusDays(3).atStartOfDay(zoneId).toInstant()))
        )
    )

    val result = createOrUpdateBudgetRealizationsWithPrint(budget, transactions)

    println("\nFinal budget realizations:")
    result.forEachIndexed { i, br ->
//        println("[$i] Start: ${br.startTime.toDate()}, End: ${br.endTime.toDate()}, Transactions: ${br.involvedTransactionsUuid}")
        println(br)
    }
}

fun monthlyBudgetRealization() {
    val zoneId = ZoneId.systemDefault()
    val startDate = LocalDate.now().minusMonths(2)
    val startTimestamp = Timestamp(Date.from(startDate.atStartOfDay(zoneId).toInstant()))

    val budget = Budget(
        uuid = "budget-3",
        name = "Langganan",
        amount = 100_000f,
        startTime = startTimestamp,
        timeRangeInDays = 30,
        recurringTypeOrdinal = EnumTimeRange.MONTHLY.ordinal,
        involvedCategoriesUuid = listOf("cat-6")
    )

    val transactions = listOf(
        Transaction(
            uuid = "trx-5",
            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
            relatedEntityId = "cat-6",
            dateAdded = Timestamp(Date.from(LocalDate.now().minusDays(10).atStartOfDay(zoneId).toInstant()))
        )
    )

    val result = createOrUpdateBudgetRealizationsWithPrint(budget, transactions)

    println("\nFinal budget realizations:")
    result.forEachIndexed { i, br ->
//        println("[$i] Start: ${br.startTime.toDate()}, End: ${br.endTime.toDate()}, Transactions: ${br.involvedTransactionsUuid}")
        println(br)
    }
}

fun yearlyBudgetRealization() {
    val zoneId = ZoneId.systemDefault()
    val startDate = LocalDate.now().minusYears(1)
    val startTimestamp = Timestamp(Date.from(startDate.atStartOfDay(zoneId).toInstant()))

    val budget = Budget(
        uuid = "budget-4",
        name = "Liburan",
        amount = 2_000_000f,
        startTime = startTimestamp,
        timeRangeInDays = 365,
        recurringTypeOrdinal = EnumTimeRange.YEARLY.ordinal,
        involvedCategoriesUuid = listOf("cat-7")
    )

    val transactions = listOf(
        Transaction(
            uuid = "trx-6",
            transactionTypeOrdinal = TransactionType.EXPENSE.ordinal,
            relatedEntityId = "cat-7",
            dateAdded = Timestamp(Date.from(LocalDate.now().minusMonths(3).atStartOfDay(zoneId).toInstant()))
        )
    )

    val result = createOrUpdateBudgetRealizationsWithPrint(budget, transactions)

    println("\nFinal budget realizations:")
    result.forEachIndexed { i, br ->
//        println("[$i] Start: ${br.startTime.toDate()}, End: ${br.endTime.toDate()}, Transactions: ${br.involvedTransactionsUuid}")
        println(br)
    }
}
