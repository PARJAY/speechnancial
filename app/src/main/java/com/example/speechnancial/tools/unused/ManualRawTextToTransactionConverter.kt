package com.example.speechnancial.tools.unused

import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.common.pemasukanKeywords
import com.example.speechnancial.common.pengeluaranKeywords

fun batchTransactionSplitterRegexDebug(input: String): List<String> {
    val words = input.split(" ")
    val result = mutableListOf<String>()
    val currentTransaction = mutableListOf<String>()
    var inNominal = false

    words.forEach { word ->
        println("Processing word: $word")
        currentTransaction.add(word)

        if (word == "rp" || word == "rp.") {
            println("Entering nominal state")
            inNominal = true
        }
        else if (
            word.startsWith("rp") && word.length > 2
            || word == "rupiah"
            || inNominal
        ) {
            println("Found a complete transaction: ${currentTransaction.joinToString(" ")}")
            result.add(currentTransaction.joinToString(" "))
            currentTransaction.clear()
            inNominal = false
        }
    }

    if (currentTransaction.isNotEmpty()) {
        println("Adding remaining transaction: ${currentTransaction.joinToString(" ")}")
        result.add(currentTransaction.joinToString(" "))
    }

    return result
}

fun batchTransactionSplitter(input: String): List<String> {
    val words = input.split(" ")
    val completeTransactionList = mutableListOf<String>()
    val currentTransaction = mutableListOf<String>()
    var nominalAmountNext = false

    words.forEach { word ->
        currentTransaction.add(word)

        if (word == "rp" || word == "rp.") nominalAmountNext = true
        else if (
            word.startsWith("rp")
                && word.length > 2
            || word == "rupiah"
            || nominalAmountNext
        ) {
            completeTransactionList.add(currentTransaction.joinToString(" "))
            currentTransaction.clear()
            nominalAmountNext = false
        }
    }

    if (currentTransaction.isNotEmpty())
        completeTransactionList.add(currentTransaction.joinToString(" "))

    return completeTransactionList
}

val input = "Beli ESP 32 rp. 100.000 breadboard 400 poin dua puluh delapan ribu rupiah dht rp 25.000 kabel jumper rp45.000"

// Data class untuk model transaksi
data class Transaction(
    var type: TransactionType = TransactionType.UNDEFINED,
    val descriptions: MutableList<Pair<String, Float>> = mutableListOf()
)

fun main() {
    val result = batchTransactionSplitter(input)

    result.forEachIndexed { index, transaction ->
        println("Deskripsi transaksi ${index + 1}: $transaction")
    }

    val transaction = Transaction()


    result.forEachIndexed { index, description ->
        if (index == 0) {
            // Identifikasi tipe transaksi dari deskripsi pertama
            transaction.type = identifyTransactionType(description)
        }

        // Ekstraksi deskripsi dan nominal
        val (desc, nominal) = extractDescriptionAndNominal(description)
        transaction.descriptions.add(Pair(desc, nominal))
    }

    // Output transaksi sah
    println("Tipe Transaksi: ${transaction.type}")
    transaction.descriptions.forEachIndexed { index, pair ->
        println("Deskripsi ${index + 1}: ${pair.first}, Nominal: ${pair.second}")
    }
}


// Identifikasi tipe transaksi
fun identifyTransactionType(description: String): TransactionType {
    return when {
        pemasukanKeywords.any { description.contains(it, ignoreCase = true) } -> TransactionType.EARNING
        pengeluaranKeywords.any { description.contains(it, ignoreCase = true) } -> TransactionType.SPENDING
        else -> TransactionType.UNDEFINED
    }
}

// Ekstraksi deskripsi dan nominal
fun extractDescriptionAndNominal(description: String): Pair<String, Float> {
    val words = description.split(" ")
    val nominal: Float
    val nominalBuilder = StringBuilder()
    val descBuilder = StringBuilder()

    var nominalFound = false
    for (word in words) {
        when {
            // Penanda "rp" atau "rupiah"
            word.startsWith("rp", ignoreCase = true) || word.equals("rupiah", ignoreCase = true) -> {
                nominalFound = true
            }

            // Nominal angka
            nominalFound && word.all { it.isDigit() || it == '.' } -> {
                nominalBuilder.append(word)
                nominalFound = false
            }

            // Nominal huruf
            nominalFound || isNominalWord(word) -> {
                nominalBuilder.append(" ").append(word)
            }

            // Jika bukan nominal, tambahkan ke deskripsi
            else -> descBuilder.append(word).append(" ")
        }
    }

    // Konversi nominal ke float
    nominal = convertToFloat(nominalBuilder.toString().trim())

    return Pair(descBuilder.toString().trim(), nominal)
}

// Deteksi kata nominal
fun isNominalWord(word: String): Boolean {
    val nominalWords = listOf(
        "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan",
        "belas", "puluh", "ratus", "ribu", "juta", "miliar", "triliun",
        "sebelas", "sepuluh", "seratus", "seribu", "sejuta"
    )
    return word in nominalWords
}

// Konversi string nominal ke float
fun convertToFloat(nominalString: String): Float {
    // Implementasi sederhana: Nominal angka langsung
    return try {
        nominalString.replace(".", "").replace(",", "").toFloat()
    } catch (e: NumberFormatException) {
        0f
    }
}