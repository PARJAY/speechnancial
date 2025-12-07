package com.example.speechnancial.tools.algoritma.working

import com.example.speechnancial.data.firebase.model.Transaction
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.tools.parseNominalToFloat
import com.google.firebase.Timestamp
import java.time.LocalDateTime

// do a process
// process expected
// for each occurrences get every value and
// if key rp, loop the next char in text index from map value
// if key rupiah, loop the previous char in text index from map value
// store every number and "." in a variable
// store else in description variable

const val SPACING_LENGTH = 1
const val RP_LENGTH_MODIFIER = 2
const val RUPIAH_LENGTH_MODIFIER = 6
const val RUPIAH_INDEX_SHIFT = 0


fun nominalDescriptionSeparator(
//    text : String = "Pagi beli Laklak sama bikang Rp2.000 habis itu ke pasar Oh sebelum ke pasar beli risol Rp3.000 habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang 11.000 rupiah setelah itu ke Tohpati di sana dapat kelepon Rp5.000",
//    text : String = "Beli 2 Indomaret UHT 950 sayur 33.800 rupiah rupiah rupiah rupiah",
    text : String = "pengeluaran parkir rp2000 pengeluaran sayur 50000000000000000000000000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah",
    occurrences: Map<String, List<Int>> = mapOf(
//        "rp" to listOf(29, 87, 226),
//        "rupiah" to listOf(174)
//        "rp" to listOf(),
//        "rupiah" to listOf(37, 44, 51, 58)
        "rp" to listOf(19),
        "rupiah" to listOf(49, 113)
    )
): MutableList<Pair<String, String>> {
    val occurrencesPair = mutableListOf<Pair<Int, String>>()

    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            occurrencesPair.add(Pair(index, key))
        }
    }

    println(text.length)
    occurrencesPair.sortBy { it.first } // Urutkan berdasarkan indeks

    val results = mutableListOf<Pair<String, String>>() // Menyimpan Pair<Deskripsi, Nominal>
    var lastIndex = 0

    occurrencesPair.forEach { (index, key) ->
        val nominal = StringBuilder()
        val isRp = key == "rp"
        var currentIndex = if (isRp) index + RP_LENGTH_MODIFIER else index - 1

        val description = if (isRp) {
            println("going here at rp?")
            text.substring(lastIndex, index).trim()
        } else {
            try {
                text.substring(lastIndex, index - (RUPIAH_LENGTH_MODIFIER + SPACING_LENGTH)).trim() // -6 untuk "rupiah"
            } catch (e : StringIndexOutOfBoundsException) {
                ""
            }
        }

        val (condition, increment, action) = when (isRp) {
            true -> Triple(
                { currentIndex < text.length },
                { currentIndex++ },
                { nominal.append(text[currentIndex]) }
            )
            false -> Triple(
                { currentIndex >= 0 },
                { currentIndex-- },
                { nominal.insert(0, text[currentIndex]) }
            )
        }

        while (condition() && (text[currentIndex].isDigit() || text[currentIndex] == '.' || text[currentIndex] == ' ')) {
            if (text[currentIndex] != ' ' && text[currentIndex] != '.') {
                action()
            }

            increment()
        }

        if (nominal.isNotEmpty()) {
            results.add(Pair(description, nominal.toString()))
            lastIndex = index + nominal.length + (if (isRp) RP_LENGTH_MODIFIER else RUPIAH_INDEX_SHIFT) + SPACING_LENGTH
        }
    }
    return results
}


// dapatkah anda membuat fungsi seperti itu yang lebih mudah dimengerti tapi tidak boleh menggunakan regex
fun nominalDescriptionSeparatorWithDebug(
    text: String = "pengeluaran parkir rp2000 pengeluaran sayur 50000000000000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah",
    occurrences: Map<String, List<Int>> = mapOf(
        "rp" to listOf(19),
        "rupiah" to listOf(59, 123)
    )
): MutableList<Pair<String, String>> {
    println("Input Text Length: ${text.length}")
    println("Occurrences: $occurrences")

    val occurrencesPair = mutableListOf<Pair<Int, String>>()

    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            occurrencesPair.add(Pair(index, key))
        }
    }

    println("Occurrences Pair (before sort): $occurrencesPair")
    occurrencesPair.sortBy { it.first } // Urutkan berdasarkan indeks
    println("Occurrences Pair (after sort): $occurrencesPair")

    val results = mutableListOf<Pair<String, String>>() // Menyimpan Pair<Deskripsi, Nominal>
    var lastIndex = 0
    println("Initial lastIndex: $lastIndex")

    occurrencesPair.forEach { (index, key) ->
        println("\n--- Processing index: $index, key: '$key' ---")
        val nominal = StringBuilder()
        val isRp = key == "rp"
        val nominalLengthModifier = if (isRp) RP_LENGTH_MODIFIER else 0 // Panjang "rp"
        val nominalIndexShift = key.length + SPACING_LENGTH // Untuk memindahkan currentIndex ke awal nominal

        var currentIndex = if (isRp) index + key.length else index - SPACING_LENGTH - 1

        val description = if (isRp) {
            println("Calculating description for 'rp' from $lastIndex to $index")
            text.substring(lastIndex, index).trim()
        } else {
            val descriptionEndIndex = index - (RUPIAH_LENGTH_MODIFIER)
            println("Calculating description for 'rupiah' from $lastIndex to $descriptionEndIndex")
            try {
                text.substring(lastIndex, descriptionEndIndex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                ""
            }
        }
        println("Extracted description: '$description'")

        val (condition, increment, action) = when (isRp) {
            true -> Triple(
                { currentIndex < text.length },
                { currentIndex++ },
                { nominal.append(text[currentIndex]) }
            )
            false -> Triple(
                { currentIndex >= 0 },
                { currentIndex-- },
                { nominal.insert(0, text[currentIndex]) }
            )
        }

        println("Starting nominal extraction at index: $currentIndex")
        while (condition() && (text[currentIndex].isDigit() || text[currentIndex] == '.' || text[currentIndex] == ' ')) {
            println("Current char at $currentIndex: '${text[currentIndex]}'")
            if (text[currentIndex] != ' ' && text[currentIndex] != '.') {
                action()
            }
            increment()
        }
        println("Extracted nominal: '$nominal'")

        if (nominal.isNotEmpty()) {
            results.add(Pair(description, nominal.toString()))
            val nominalLength = nominal.length
            if (isRp) {
                val moveAfter = index + key.length + SPACING_LENGTH + nominalLength
                println("Moving lastIndex (for rp) from $lastIndex to $moveAfter")
                lastIndex = moveAfter
            } else {
                val moveAfter = index + key.length + SPACING_LENGTH
                println("Moving lastIndex (for rupiah) from $lastIndex to $moveAfter")
                lastIndex = moveAfter
            }
        } else {
            println("Nominal is empty, skipping update of lastIndex")
        }
        println("Current results: $results")
        println("Current lastIndex: $lastIndex")
    }

    println("\n--- Final Results ---")
    println(results)
    return results
}


data class TransactionResult(
    val description: String,
    val nominal: String,
    val transactionType: TransactionType
)

fun findTransactionKindFromDescription(firstWord: String): TransactionType {
    return when (firstWord.lowercase()) {
        "pengeluaran" -> TransactionType.EXPENSE
        "pemasukan" -> TransactionType.INCOME
        "transfer" -> TransactionType.REGULAR_TRANSFER
        "setor" -> TransactionType.SAVING_DEPOSIT
        "tarik" -> TransactionType.SAVING_WITHDRAWAL
        "hutang" -> TransactionType.DEBT_PAYMENT
        "piutang" -> TransactionType.RECEIVABLE_PAYMENT
        else -> TransactionType.UNDEFINED
    }
}

fun inputtedTextToTransactionsConverter(
    text: String = "pengeluaran parkir rp2000 pengeluaran sayur 50000000000000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah",
    occurrences: Map<String, List<Int>> = mapOf(
        "rp" to listOf(19),
        "rupiah" to listOf(59, 123)
    )
): List<Transaction> {
    val now = LocalDateTime.now()

    val occurrencesPair = mutableListOf<Pair<Int, String>>()

    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            occurrencesPair.add(Pair(index, key))
        }
    }

    occurrencesPair.sortBy { it.first }

    val transactions = mutableListOf<Transaction>()
    val currentTransactionDetails = mutableMapOf<String, Float>()
    var currentTransactionType = TransactionType.UNDEFINED
    var lastIndex = 0

    occurrencesPair.forEach { (index, key) ->
        val nominalStr = StringBuilder()
        val isRp = key == "rp"

        var currentIndex = if (isRp) index + key.length else index - SPACING_LENGTH - 1

        val description = if (isRp) {
            text.substring(lastIndex, index).trim()
        } else {
            val descriptionEndIndex = index - (RUPIAH_LENGTH_MODIFIER)
            try {
                text.substring(lastIndex, descriptionEndIndex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                ""
            }
        }

        val (condition, increment, action) = when (isRp) {
            true -> Triple(
                { currentIndex < text.length },
                { currentIndex++ },
                { nominalStr.append(text[currentIndex]) }
            )
            false -> Triple(
                { currentIndex >= 0 },
                { currentIndex-- },
                { nominalStr.insert(0, text[currentIndex]) }
            )
        }

        while (condition() && (text[currentIndex].isDigit() || text[currentIndex] == '.' || text[currentIndex] == ' ')) {
            if (text[currentIndex] != ' ' && text[currentIndex] != '.') {
                action()
            }
            increment()
        }

        if (nominalStr.isNotEmpty()) {
            val transactionType = description.split(" ").firstOrNull()?.let { findTransactionKindFromDescription(it) } ?: TransactionType.UNDEFINED
            val nominalValue = parseNominalToFloat(nominalStr.toString())

            if (transactionType != TransactionType.UNDEFINED) {
                if (currentTransactionDetails.isNotEmpty()) {
                    transactions.add(
                        Transaction(
                            details = currentTransactionDetails.toMap(),
                            total = currentTransactionDetails.values.sum(),
                            transactionTypeOrdinal = currentTransactionType.ordinal,
                            fullText = currentTransactionDetails.keys.joinToString(" "),
                            isValid = currentTransactionDetails.isNotEmpty(),
//                            dateAdded = Timestamp(now.atZone(ZoneId.systemDefault()).toInstant().epochSecond, 0)
                            dateAdded = Timestamp.now()
                        )
                    )
                    currentTransactionDetails.clear()
                }
                currentTransactionType = transactionType
                currentTransactionDetails[description] = nominalValue
            } else {
                currentTransactionDetails[description] = nominalValue
            }

            val nominalLength = nominalStr.length
            lastIndex = if (isRp) {
                index + key.length + SPACING_LENGTH + nominalLength
            } else {
                index + key.length + SPACING_LENGTH
            }
        }
    }

    if (currentTransactionDetails.isNotEmpty()) {
        transactions.add(
            Transaction(
                details = currentTransactionDetails.toMap(),
                total = currentTransactionDetails.values.sum(),
                transactionTypeOrdinal = currentTransactionType.ordinal,
                fullText = currentTransactionDetails.keys.joinToString(" "),
                isValid = currentTransactionDetails.isNotEmpty(),
                dateAdded = Timestamp.now()
            )
        )
    }

    return transactions
}

fun findTransactionKindFromDescriptionWithDebug(firstWord: String): TransactionType {
    println("[findTransactionKindFromDescription] Memeriksa kata pertama: $firstWord")
    return when (firstWord.lowercase()) {
        "pengeluaran" -> {
            println("  -> Branch: pengeluaran (EXPENSE)")
            TransactionType.EXPENSE
        }
        "pemasukan" -> {
            println("  -> Branch: pemasukan (INCOME)")
            TransactionType.INCOME
        }
        "transfer" -> {
            println("  -> Branch: transfer (REGULAR_TRANSFER)")
            TransactionType.REGULAR_TRANSFER
        }
        "setor" -> {
            println("  -> Branch: setor (SAVING_DEPOSIT)")
            TransactionType.SAVING_DEPOSIT
        }
        "tarik" -> {
            println("  -> Branch: tarik (SAVING_WITHDRAWAL)")
            TransactionType.SAVING_WITHDRAWAL
        }
        "hutang" -> {
            println("  -> Branch: hutang (DEBT_PAYMENT)")
            TransactionType.DEBT_PAYMENT
        }
        "piutang" -> {
            println("  -> Branch: piutang (RECEIVABLE_PAYMENT)")
            TransactionType.RECEIVABLE_PAYMENT
        }
        else -> {
            println("  -> Branch: default (UNDEFINED)")
            TransactionType.UNDEFINED
        }
    }
}

fun inputtedTextToTransactionsConverterWithDebug(
    text: String,
    occurrences: Map<String, List<Int>>
): List<Transaction> {
    println("[inputtedTextToTransactionsConverter] Mulai memproses teks: \"$text\"")
    println("  Jumlah pattern ditemukan: ${occurrences.size}")

    val occurrencesPair = mutableListOf<Pair<Int, String>>()
    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            println("  Menambahkan pasangan index $index dengan pattern '$key'")
            occurrencesPair.add(Pair(index, key))
        }
    }

    occurrencesPair.sortBy { it.first }
    println("  Urutan occurrence setelah sort: $occurrencesPair")

    val transactions = mutableListOf<Transaction>()
    val currentTransactionDetails = mutableMapOf<String, Float>()
    var currentTransactionType = TransactionType.UNDEFINED
    var lastIndex = 0

    occurrencesPair.forEach { (index, key) ->
        println(">> Memproses occurrence: key=$key, index=$index")
        val nominalStr = StringBuilder()
        val isRp = key == "rp"
        println("   isRp? $isRp")

        var currentIndex = if (isRp) index + key.length else index - SPACING_LENGTH - 1

        val description = if (isRp) {
            println("   Branch: isRp = true → ambil substring dari lastIndex=$lastIndex sampai index=$index")
            text.substring(lastIndex, index).trim()
        } else {
            println("   Branch: isRp = false → hitung descriptionEndIndex")
            val descriptionEndIndex = index - (RUPIAH_LENGTH_MODIFIER)
            try {
                text.substring(lastIndex, descriptionEndIndex).trim()
            } catch (e: StringIndexOutOfBoundsException) {
                println("   Exception: StringIndexOutOfBoundsException → deskripsi kosong")
                ""
            }
        }

        val (condition, increment, action) = when (isRp) {
            true -> Triple(
                { currentIndex < text.length },
                { currentIndex++ },
                { nominalStr.append(text[currentIndex]) }
            )
            false -> Triple(
                { currentIndex >= 0 },
                { currentIndex-- },
                { nominalStr.insert(0, text[currentIndex]) }
            )
        }

        println("   Memulai loop baca nominal...")
        while (condition() && (text[currentIndex].isDigit() || text[currentIndex] == '.' || text[currentIndex] == ' ')) {
            if (text[currentIndex] != ' ' && text[currentIndex] != '.') {
                action()
            }
            increment()
        }
        println("   Nominal ditemukan: '$nominalStr'")

        if (nominalStr.isNotEmpty()) {
            val transactionType = description.split(" ").firstOrNull()
                ?.let { findTransactionKindFromDescription(it) }
                ?: TransactionType.UNDEFINED
            val nominalValue = parseNominalToFloat(nominalStr.toString())
            println("   transactionType=$transactionType, nominalValue=$nominalValue")

            if (transactionType != TransactionType.UNDEFINED) {
                println("   Branch: transactionType terdefinisi")
                if (currentTransactionDetails.isNotEmpty()) {
                    println("   Menambahkan transaksi sebelumnya ke daftar")
                    transactions.add(
                        Transaction(
                            details = currentTransactionDetails.toMap(),
                            total = currentTransactionDetails.values.sum(),
                            transactionTypeOrdinal = currentTransactionType.ordinal,
                            fullText = currentTransactionDetails.keys.joinToString(" "),
                            isValid = currentTransactionDetails.isNotEmpty(),
                            dateAdded = Timestamp.now()
                        )
                    )
                    currentTransactionDetails.clear()
                }
                currentTransactionType = transactionType
                currentTransactionDetails[description] = nominalValue
            } else {
                println("   Branch: transactionType UNDEFINED → menambahkan detail ke transaksi saat ini")
                currentTransactionDetails[description] = nominalValue
            }

            val nominalLength = nominalStr.length
            lastIndex = if (isRp) {
                index + key.length + SPACING_LENGTH + nominalLength
            } else {
                index + key.length + SPACING_LENGTH
            }
            println("   lastIndex diupdate menjadi $lastIndex")
        }
    }

    if (currentTransactionDetails.isNotEmpty()) {
        println("Menambahkan transaksi terakhir ke daftar")
        transactions.add(
            Transaction(
                details = currentTransactionDetails.toMap(),
                total = currentTransactionDetails.values.sum(),
                transactionTypeOrdinal = currentTransactionType.ordinal,
                fullText = currentTransactionDetails.keys.joinToString(" "),
                isValid = currentTransactionDetails.isNotEmpty(),
                dateAdded = Timestamp.now()
            )
        )
    }

    return transactions
}


// Menghapus duplikat dan mencetak hasil
// Output: 2.000, 3.000, 5.000, 11000

// output expected
// Pagi beli Laklak sama bikang, 2.000
// habis itu ke pasar Oh sebelum ke pasar beli risol, 3.000
// habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang, 11.000
// setelah itu ke Tohpati di sana dapat kelepon Rp5.000

fun main() {
    nominalDescriptionSeparatorWithDebug().distinct().forEach {
        println(it)
    }
}