package com.example.speechnancial.tools

import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import com.example.speechnancial.tools.algoritma.working.nominalDescriptionSeparator
import com.google.firebase.Timestamp

// todo : check what different before performing regex Regex denial of Service
//  compare user raw input with previousTransaction to see where is the edit happened
//  the only way we know is
//  and lastly, only do and apply the change to the edited
//  a
//  val friendlyToEditRawText = details.joinToString(separator = " ") { detail ->
//     "${detail.description} ${detail.nominal.toInt()} rupiah"
//  }

// todo : pake boyer moore
//  untested

// saya ingin membuat list of transaction dari string yang diberikan
// pertama buatlah test case

//  saat kata masuk kedalam fungsi
//  kata rp[nominal] atau [nominal] rupiah akan menjadi akhir dari sebuah transaksi dan di deteksi oleh algoritma boyerMoore dengan fungsi yang diberikan

//  pengeluaran parkir rp2000 pengeluaran sayur 5000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah
//  kata paling awal akan menentukan transactionKind

// TransactionKind.REGULAR_EXPENSE -> "Pengeluaran"
// TransactionKind.REGULAR_INCOME -> "Pemasukan"
// TransactionKind.REGULAR_TRANSFER -> "Transfer"

// TransactionKind.SAVING_DEPOSIT -> "Setoran"
// TransactionKind.SAVING_WITHDRAWAL -> "Penarikan"

// TransactionKind.DEBT_PAYMENT -> "Hutang"
// TransactionKind.RECEIVABLE_PAYMENT -> "Piutang"

//  sisa kata diantara itu akan menjadi deskripsi

// jika masih ada kata setelahnya, akan menjadi transaksi baru pada list of Transaction

fun createTransactionWithBoyerMooreFromInputNew(
    userRawInput: String,
    previousTransactionId: String = ""
): Transaction {
    val extractedNominalAndDescriptionList = boyerMooreMultiplePatternsWithReturn(userRawInput)
    val results = nominalDescriptionSeparator(userRawInput, extractedNominalAndDescriptionList)

    val details = results.associate { (description, nominal) ->
        description to parseNominalToFloat(nominal)
    }

    val friendlyToEditRawText = details.entries.joinToString(" ") { (description, nominal) ->
        "$description ${nominal.toInt()} rupiah"
    }.ifEmpty { userRawInput }

    var transactionType: TransactionType = TransactionType.UNDEFINED

    val firstDetail: Pair<String, String>? = results.firstOrNull()

    if (firstDetail != null) {
        val (description) = firstDetail
        transactionType = findTransactionKind(description.split(" ")[0].lowercase())
    }

    return Transaction(
        uuid = previousTransactionId,
        fullText = friendlyToEditRawText,
        transactionTypeOrdinal = transactionType.ordinal,
        details = details.filterKeys { it.isNotBlank() }.ifEmpty { null }, // Filter out blank descriptions
        total = details.values.sum(),
        dateAdded = Timestamp.now(),
        isNeedRevise = false,
        isValid = details.any { (description, nominal) -> description.isNotBlank() && nominal > 0 },
        isFromSmartwatch = false
    )
}

fun findTransactionKind(firstWord: String): TransactionType {
    return when (firstWord) {
        "pengeluaran" -> TransactionType.EXPENSE
        "pemasukan" -> TransactionType.INCOME
        "transfer" -> TransactionType.REGULAR_TRANSFER
        "setoran" -> TransactionType.SAVING_DEPOSIT
        "penarikan" -> TransactionType.SAVING_WITHDRAWAL
        "hutang" -> TransactionType.DEBT_PAYMENT
        else -> TransactionType.UNDEFINED
    }
}

// harus menjelaskan fungsi boyerMooreMultiplePatternsWithReturn pada AI
// harus membuat AI untuk mengenerate Test case yang lebih kompleks seperti multiple Transaction pada input string yang diberikan
// karena fungsi ini akan digunakan pada create dan update saya harus membuat fungsi ini fleksibel, dan menjelaskan alur data pada AInya

fun main() {
    val input1 = "pengeluaran parkir rp2000 pengeluaran sayur 5000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah"
    val transactions1 = mutableListOf<Transaction>()
    val remainingInput1 = input1
    val previousId1 = ""

    val transaction = createTransactionWithBoyerMooreFromInputNew(remainingInput1, previousId1)
    print(transaction.details)
}