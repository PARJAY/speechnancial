package com.example.speechnancial.tools

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import java.time.LocalDateTime
import kotlin.math.log

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


    val userInputTestCase1 = "Beli sabun 5000 rupiah sampo 15000 rupiah deterjen 20000 rupiah"

    val userInputTestCase2 = "Beli sabun 5000 rupiah sampo pantene 25000 rupiah deterjen 20000 rupiah"
    val previousTransactionTestCase2 = Transaction(
        details = listOf(
            TransactionDetail("Beli sabun ", 5000f),
            TransactionDetail("sampo ", 15000f),
            TransactionDetail("deterjen ", 20000f),
        )
    )

    createTransactionFromInput(userInputTestCase1).details?.forEach { transactionDetail ->
        println(transactionDetail.description)
        println(transactionDetail.nominal)
        println()
    }

    createTransactionFromInput(
        userInputTestCase2,
        0
    ).details?.forEach { transactionDetail ->
        println(transactionDetail.description)
        println(transactionDetail.nominal)
        println()
    }
}

// todo : check what different before performing regex Regex denial of Service
//  compare user raw input with previousTransaction to see where is the edit happened
//  the only way we know is
//  and lastly, only do and apply the change to the edited
//  a
//  val friendlyToEditRawText = details.joinToString(separator = " ") { detail ->
//     "${detail.description} ${detail.nominal.toInt()} rupiah"
//  }

fun createTransactionFromInput(
    userRawInput: String,
    previousTransactionId: Int = 0
) : Transaction {
    val extractedNominalList : List<String> = nominalExtractorRegex(userRawInput).toList()
    val extractedDescriptionList: MutableList<Pair<String, String>> = descriptionExtractorNew(userRawInput, extractedNominalList.asSequence())

    val details = extractedDescriptionList.map { (description, nominal) ->
        TransactionDetail(
            description = description,
            nominal = parseNominalToFloat(nominal)
        )
    }

    var transactionType: TransactionType = TransactionType.UNDEFINED
    val firstDetail : Pair<String, String>? = extractedDescriptionList.firstOrNull()
    if (firstDetail != null) {
        val (description) = firstDetail
        transactionType = findTransactionType(description.split(" ")[0].lowercase())
    }

    val friendlyToEditRawText = details.joinToString(separator = " ") { detail ->
        "${detail.description} ${detail.nominal.toInt()} rupiah"
    }.ifEmpty { userRawInput }

//    Log.d("friendlyToEditRawText", friendlyToEditRawText)

    return Transaction(
        id = previousTransactionId,
        rawText = friendlyToEditRawText,
        type = transactionType,
        details = details,
        total = details.sumOf { it.nominal.toInt() }.toFloat(),
        createdAt = LocalDateTime.now(),
        isReviseNeeded = details.any { !it.emptyChecker() },
        isValid = transactionType != TransactionType.UNDEFINED && details.all { it.emptyChecker() }
    )
}

fun createTransactionFromInputWithDebug(userRawInput: String, transaction : MutableState<Transaction>): Transaction {
    val extractedNominalList = nominalExtractorRegex(userRawInput).toList()
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

    return  Transaction(
        rawText = userRawInput,
        type = transactionType,
        details = details,
        createdAt = LocalDateTime.now(),
        isReviseNeeded = details.any { !it.emptyChecker() },
        isValid = transactionType != TransactionType.UNDEFINED && details.all { it.emptyChecker() }
    )
}
