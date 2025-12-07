package com.example.speechnancial.tools

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.speechnancial.common.TransactionTypeOld
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail
import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import com.example.speechnancial.tools.algoritma.working.nominalDescriptionSeparator
import com.google.firebase.Timestamp
import java.time.LocalDateTime

fun main() {
//    val text =
//        "Beli ESP 32 rp100 miliar 100 juta 100.000,000 " +
//        "dht rp 25.000 " +
//        "kabel jumper rp. 45.000.000.000.000 " +
//        "breadboard 400 poin rp 28.000 " +
//        "cuci uang korupsi bambang tambang timah rp 12 miliar 11 juta 19.111 " +
//        "cuci uang korupsi bambang tambang timah 12 miliar 11 juta 19.111 rupiah"
//
//    val result = createTransactionFromInput(text)
//    result.detailsRoom?.forEach { transactionDetail ->
//        println(transactionDetail.description)
//        println(transactionDetail.nominal)
//        println()
//    }

//    val userInputTestCase1 = "Beli sabun 5000 rupiah sampo 15000 rupiah deterjen 20000 rupiah"
    val userInputTestCase1 = "pengeluaran parkir rp2000 pengeluaran sayur 5000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah"

    val userInputTestCase2 = "Beli sabun 5000 rupiah sampo pantene 25000 rupiah deterjen 20000 rupiah"
    val previousTransactionTestCase2 = Transaction(
        detailsRoom = listOf(
            TransactionDetail("Beli sabun ", 5000f),
            TransactionDetail("sampo ", 15000f),
            TransactionDetail("deterjen ", 20000f),
        )
    )

    createTransactionFromInput(userInputTestCase1).detailsRoom?.forEach { transactionDetail ->
        println(transactionDetail.description)
        println(transactionDetail.nominal)
        println()
    }

    createTransactionFromInput(
        userInputTestCase2,
        ""
    ).detailsRoom?.forEach { transactionDetail ->
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


// TransactionKind.REGULAR_EXPENSE -> "Pengeluaran"
// TransactionKind.REGULAR_INCOME -> "Pemasukan"
// TransactionKind.REGULAR_TRANSFER -> "Transfer"

// TransactionKind.SAVING_DEPOSIT -> "Setoran"
// TransactionKind.SAVING_WITHDRAWAL -> "Penarikan"

// TransactionKind.DEBT_PAYMENT -> "Hutang"
// TransactionKind.RECEIVABLE_PAYMENT -> "Piutang"

fun createTransactionFromInput(
    userRawInput: String,
    previousTransactionId: String = ""
): Transaction {
    val extractedNominalList: List<String> = nominalExtractorRegex(userRawInput).toList()
    val extractedDescriptionList: MutableList<Pair<String, String>> =
        descriptionExtractorNew(userRawInput, extractedNominalList.asSequence())

    val detailsRoom = extractedDescriptionList.map { (description, nominal) ->
        TransactionDetail(
            description = description,
            nominal = parseNominalToFloat(nominal)
        )
    }

    val details: MutableMap<String, Float> = mutableMapOf()

    detailsRoom.forEach { detail ->
        details[detail.description] = detail.nominal
    }

    var transactionTypeOld: TransactionTypeOld = TransactionTypeOld.UNDEFINED
    val firstDetail: Pair<String, String>? = extractedDescriptionList.firstOrNull()
    if (firstDetail != null) {
        val (description) = firstDetail
        transactionTypeOld = findTransactionType(description.split(" ")[0].lowercase())
    }

    val friendlyToEditRawText = detailsRoom.joinToString(separator = " ") { detail ->
        "${detail.description} ${detail.nominal.toInt()} rupiah"
    }.ifEmpty { userRawInput }

//    Log.d("friendlyToEditRawText", friendlyToEditRawText)

    return Transaction(
        id = previousTransactionId,
        rawText = friendlyToEditRawText,
        type = transactionTypeOld,
        detailsRoom = detailsRoom,
        details = details,
        total = detailsRoom.sumOf { it.nominal.toInt() }.toFloat(),
        createdAt = Timestamp.now(),
        createdAtRoom = LocalDateTime.now(),
        isReviseNeeded = detailsRoom.any { !it.emptyChecker() },
        isValid = transactionTypeOld != TransactionTypeOld.UNDEFINED && detailsRoom.all { it.emptyChecker() }
    )
}

// todo : pake boyer moore
//  untested

//
fun createTransactionWithBoyerMooreFromInput(
    userRawInput: String,
    previousTransactionId: String = ""
): Transaction {
//    val extractedNominalList: List<String> = nominalExtractorRegex(userRawInput).toList()
//    val extractedDescriptionList: MutableList<Pair<String, String>> =
//        descriptionExtractorNew(userRawInput, extractedNominalList.asSequence())
    val extractedNominalAndDescriptionList = boyerMooreMultiplePatternsWithReturn(userRawInput)

    val results = nominalDescriptionSeparator(userRawInput, extractedNominalAndDescriptionList)

    val details = results.associate { (description, nominal) ->
        description to parseNominalToFloat(nominal)
    }

    val friendlyToEditRawText = details.entries.joinToString(" ") { (description, nominal) ->
        "$description ${nominal.toInt()} rupiah"
    }.ifEmpty { userRawInput }

//    val detailsRoom = results.map  { (description, nominal) ->
//        TransactionDetail(
//            description = description,
//            nominal = parseNominalToFloat(nominal)
//        )
//    }
//
//    val friendlyToEditRawText = detailsRoom.joinToString(separator = " ") { detail ->
//        "${detail.description} ${detail.nominal.toInt()} rupiah"
//    }.ifEmpty { userRawInput }
//
//    val details: MutableMap<String, Float> = mutableMapOf()
//
//    results.map  { (description, nominal) ->
//        details[description] = parseNominalToFloat(nominal)
//    }

    var transactionTypeOld: TransactionTypeOld = TransactionTypeOld.UNDEFINED
//    val firstDetail: Pair<String, String>? = extractedDescriptionList.firstOrNull()

    val firstDetail: Pair<String, String>? = results.firstOrNull()

    if (firstDetail != null) {
        val (description) = firstDetail
        transactionTypeOld = findTransactionType(description.split(" ")[0].lowercase())
    }

//    Log.d("friendlyToEditRawText", friendlyToEditRawText)

//    return Transaction(
//        id = previousTransactionId,
//        rawText = friendlyToEditRawText,
//        type = transactionType,
////        detailsRoom = detailsRoom,
//        details = details,
//        total = detailsRoom.sumOf { it.nominal.toInt() }.toFloat(),
//        createdAt = Timestamp.now(),
////        createdAtRoom = LocalDateTime.now(),
//        isReviseNeeded = detailsRoom.any { !it.emptyChecker() },
//        isValid = transactionType != TransactionType.UNDEFINED && detailsRoom.all { it.emptyChecker() },
//        isFromSmartwatch = false
//    )

    // will i need the raw text?
    // old transaction
    return Transaction(
        id = previousTransactionId,
        rawText = friendlyToEditRawText,
        type = transactionTypeOld,

        details = details, // Use the details Map directly
        total = details.values.sumOf { it.toInt() }.toFloat(), // Sum from the Map values
        createdAt = Timestamp.now(),

        isReviseNeeded = false, // di deklarasi pada viewmodel

        isValid = details.any { (description, nominal) ->
            description.isNotEmpty() && nominal > 0
        }, // Check with details Map
        isFromSmartwatch = false
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

    var transactionTypeOld: TransactionTypeOld = TransactionTypeOld.UNDEFINED
    val firstDetail = extractedDescriptionList.firstOrNull()
    if (firstDetail != null) {
        val (description) = firstDetail
        transactionTypeOld = findTransactionType(description.split(" ")[0].lowercase())
    }

    return  Transaction(
        rawText = userRawInput,
        type = transactionTypeOld,
        detailsRoom = details,
        createdAtRoom = LocalDateTime.now(),
        isReviseNeeded = details.any { !it.emptyChecker() },
        isValid = transactionTypeOld != TransactionTypeOld.UNDEFINED && details.all { it.emptyChecker() }
    )
}
