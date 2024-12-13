package com.example.speechnancial.tools

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import java.time.LocalDateTime
import java.util.Date

fun main() {
    val text =
        "Beli ESP 32 rp100 miliar 100 juta 100.000,000 " +
        "dht rp 25.000 " +
        "kabel jumper rp. 45.000.000.000.000 " +
        "breadboard 400 poin rp 28.000 " +
        "cuci uang korupsi bambang tambang timah rp 12 miliar 11 juta 19.111 " +
        "cuci uang korupsi bambang tambang timah 12 miliar 11 juta 19.111 rupiah"

    val result = createTransactionFromInput(text)
    result.details?.forEach { transactionDetail ->
        println(transactionDetail.description)
        println(transactionDetail.nominal)
        println()
    }
}

fun createTransactionFromInput(
    userRawInput: String
) : Transaction {
    val extractedNominalList = nominalExtractor(userRawInput).toList()
    val extractedDescriptionList = descriptionExtractor(userRawInput, extractedNominalList.asSequence())

    val details = extractedDescriptionList.map { (description, nominal) ->
        TransactionDetail(
            description = description,
            nominal = parseNominalToFloat(nominal)
        )
    }

    var transactionType: TransactionType = TransactionType.UNDEFINED
    val firstDetail = extractedDescriptionList.firstOrNull()
    if (firstDetail != null) {
        val (description) = firstDetail
        transactionType = findTransactionType(description.split(" ")[0].lowercase())
    }

    return Transaction(
        rawText = userRawInput,
        type = transactionType,
        details = details,
        total = details.sumOf { it.nominal.toInt() }.toFloat(),
        createdAt = LocalDateTime.now(),
        isReviseNeeded = details.any { !it.emptyChecker() },
        isValid = transactionType != TransactionType.UNDEFINED && details.all { it.emptyChecker() }
    )
}

fun createTransactionFromInputWithDebug(userRawInput: String, transaction : MutableState<Transaction>): Transaction {
    val extractedNominalList = nominalExtractor(userRawInput).toList()
    val extractedDescriptionList = descriptionExtractor(userRawInput, extractedNominalList.asSequence())

    extractedNominalList.forEach {
        Log.d("extractedNominalList", it)
    }

    val details = extractedDescriptionList.map { (description, nominal) ->
        TransactionDetail(
            description = description,
            nominal = parseNominalToFloat(nominal)
        )
    }

    var transactionType: TransactionType = TransactionType.UNDEFINED
    val firstDetail = extractedDescriptionList.firstOrNull()
    if (firstDetail != null) {
        val (description) = firstDetail
        transactionType = findTransactionType(description.split(" ")[0].lowercase())
    }

    val transaction = Transaction(
        rawText = userRawInput,
        type = transactionType,
        details = details,
        createdAt = LocalDateTime.now(),
        isReviseNeeded = details.any { !it.emptyChecker() },
        isValid = transactionType != TransactionType.UNDEFINED && details.all { it.emptyChecker() }
    )

    return transaction
}
