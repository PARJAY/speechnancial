package com.example.speechnancial.tools

import android.os.Environment
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.EnumTimeRange
import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.google.firebase.Timestamp
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

fun String.floatOrString() = toFloatOrNull() ?: this

class Util {
    companion object {
        fun parseToFloatSystem(inputedFormattedNumber: String): Float {
            return inputedFormattedNumber.replace(".", "").toFloat()
        }

        fun logDataFlowToFile(tag: String, message: String) {
            try {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "dataflow_log.txt"
                )

                val timestamp = SimpleDateFormat("dd-MM HH:mm:ss", Locale.getDefault()).format(Date())
                file.appendText("[$timestamp][$tag] $message\n")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun formatTimestampToDayMonth(timestamp: Timestamp): String {
            val date = timestamp.toDate() // Convert Timestamp to Date
            val sdf = SimpleDateFormat("d MMMM", Locale.getDefault()) // Format: "4 April"
            return sdf.format(date)
        }

        fun formatTimestampToHourMinute(timestamp: Timestamp): String {
            val date = timestamp.toDate()
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(date)
        }

        fun outcomeOverflowHandler(percentage: Float) : Float {
            return if (percentage < 0 ) (percentage.absoluteValue * 100 + 100)
            else percentage * 100
        }

        fun getNextStartTime(currentStart: LocalDate, type: EnumTimeRange, customDays: Int): LocalDate {
            return when (type) {
                EnumTimeRange.DAILY -> currentStart.plusDays(1)
                EnumTimeRange.WEEKLY -> currentStart.plusWeeks(1)
                EnumTimeRange.MONTHLY -> currentStart.plusMonths(1)
                EnumTimeRange.YEARLY -> currentStart.plusYears(1)
                EnumTimeRange.CUSTOM -> currentStart.plusDays(customDays.toLong())
                EnumTimeRange.NOT_RECURRING -> currentStart // tidak digunakan
            }
        }

        fun createOrUpdateBudgetRealizations(
            budget: Budget,
            transactions: List<Transaction>
        ): List<BudgetRealization> {
            val budgetRealizations = mutableListOf<BudgetRealization>()
            val zoneId = ZoneId.systemDefault()

            val today = LocalDate.now()
            var currentStartDate = budget.startTime.toDate().toInstant().atZone(zoneId).toLocalDate()
            val recurringType = EnumTimeRange.entries[budget.recurringTypeOrdinal]

            while (!currentStartDate.isAfter(today)) {
                val currentEndDate = when (recurringType) {
                    EnumTimeRange.MONTHLY -> currentStartDate.plusMonths(1).minusDays(1)
                    EnumTimeRange.YEARLY -> currentStartDate.plusYears(1).minusDays(1)
                    EnumTimeRange.DAILY -> currentStartDate
                    EnumTimeRange.WEEKLY -> currentStartDate.plusDays(6)
                    EnumTimeRange.CUSTOM -> currentStartDate.plusDays(budget.timeRangeInDays.toLong() - 1)
                    EnumTimeRange.NOT_RECURRING -> break
                }

                val startDateTime = currentStartDate.atStartOfDay(zoneId).toInstant()
                val endDateTime = currentEndDate.atTime(LocalTime.MAX).atZone(zoneId).toInstant()


                val involvedTransactions = transactions.filter { transaction ->
                    val involvedCategoryUuids = budget.involvedCategoriesUuid ?: emptyList()
                    val txInstant = transaction.dateAdded.toDate().toInstant()
                    val result = (transaction.transactionTypeOrdinal == TransactionType.INCOME.ordinal ||
                            transaction.transactionTypeOrdinal == TransactionType.EXPENSE.ordinal) &&
                            transaction.relatedEntityId in involvedCategoryUuids &&
                            !txInstant.isBefore(startDateTime) &&
                            !txInstant.isAfter(endDateTime)
                    result
                }.map { it.uuid }

                val realization = BudgetRealization(
                    progressAmount = 0f,
                    startTime = Timestamp(Date.from(startDateTime)),
                    endTime = Timestamp(Date.from(endDateTime)),
                    involvedTransactionsUuid = involvedTransactions,
                    isDeleted = false
                )

                budgetRealizations.add(realization)
                currentStartDate = getNextStartTime(currentStartDate, recurringType, budget.timeRangeInDays)
            }

            return budgetRealizations
        }

        fun createOrUpdateBudgetRealizationsWithPrint(
            budget: Budget,
            transactions: List<Transaction>
        ): List<BudgetRealization> {
            val budgetRealizations = mutableListOf<BudgetRealization>()
            val zoneId = ZoneId.systemDefault()

            val today = LocalDate.now()
            var currentStartDate = budget.startTime.toDate().toInstant().atZone(zoneId).toLocalDate()
            val recurringType = EnumTimeRange.entries[budget.recurringTypeOrdinal]

            println("Starting createOrUpdateBudgetRealizations")
            println("Budget start date: $currentStartDate, today: $today")

            while (!currentStartDate.isAfter(today)) {
                val currentEndDate = when (recurringType) {
                    EnumTimeRange.MONTHLY -> currentStartDate.plusMonths(1).minusDays(1)
                    EnumTimeRange.YEARLY -> currentStartDate.plusYears(1).minusDays(1)
                    EnumTimeRange.DAILY -> currentStartDate
                    EnumTimeRange.WEEKLY -> currentStartDate.plusDays(6)
                    EnumTimeRange.CUSTOM -> currentStartDate.plusDays(budget.timeRangeInDays.toLong() - 1)
                    EnumTimeRange.NOT_RECURRING -> {
                        println("Recurring type is NOT_RECURRING. Breaking loop.")
                        break
                    }
                }

                val startDateTime = currentStartDate.atStartOfDay(zoneId).toInstant()
                val endDateTime = currentEndDate.atTime(LocalTime.MAX).atZone(zoneId).toInstant()

                println("Generating realization from $currentStartDate to $currentEndDate")

                val involvedTransactions = transactions.filter { transaction ->
                    val involvedCategoryUuids = budget.involvedCategoriesUuid ?: emptyList()
                    val txInstant = transaction.dateAdded.toDate().toInstant()
                    val result = (transaction.transactionTypeOrdinal == TransactionType.INCOME.ordinal ||
                            transaction.transactionTypeOrdinal == TransactionType.EXPENSE.ordinal) &&
                            transaction.relatedEntityId in involvedCategoryUuids &&
                            !txInstant.isBefore(startDateTime) &&
                            !txInstant.isAfter(endDateTime)

                    if (result) {
                        println("Included transaction ${transaction.uuid} dated ${transaction.dateAdded.toDate()}")
                    }

                    result
                }.map { it.uuid }

                println("Total involved transactions: ${involvedTransactions.size}")

                val realization = BudgetRealization(
                    progressAmount = 0f,
                    startTime = Timestamp(Date.from(startDateTime)),
                    endTime = Timestamp(Date.from(endDateTime)),
                    involvedTransactionsUuid = involvedTransactions,
                    isDeleted = false
                )

                budgetRealizations.add(realization)

                println("BudgetRealization created: start=${realization.startTime.toDate()}, end=${realization.endTime.toDate()}")

                currentStartDate = getNextStartTime(currentStartDate, recurringType, budget.timeRangeInDays)
                println("Next start date: $currentStartDate")
            }

            println("Finished creating ${budgetRealizations.size} budget realizations.")
            return budgetRealizations
        }
    }
}