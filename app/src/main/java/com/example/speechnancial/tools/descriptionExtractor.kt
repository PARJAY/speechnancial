package com.example.speechnancial.tools

fun main() {
    val textTestCase1 =
        "Beli ESP 32 rp100 miliar 100 juta 100.000,000 " +
        "dht rp 25.000 " +
        "kabel jumper rp. 45.000.000.000.000 "

    val nominalsTestCase1 = sequenceOf(
        "rp100 miliar 100 juta 100.000,000",
        "rp 25.000",
        "rp. 45.000.000.000.000",
    )

    /* test case 1 expected result :
    // MutableList<Pair<String, String>> of
    // [
    //      (Beli ESP 32, rp100 miliar 100 juta 100.000,000 ),
    //      (dht, rp 25.000),
    //      (kabel jumper, rp. 45.000.000.000.000 )
    // ]
    */

    val textTestCase2 = "Bayar parkir 2000 rupiah Bayar parkir 2000 rupiah"

    val nominalsTestCase2 = sequenceOf(
        "2000 rupiah",
        "2000 rupiah",
    )

    val textTestCase3 = "Beng-beng Kantin Sekolah kampus Unud 2 buah rp5.000"
    val nominalsTestCase3 = sequenceOf(
        "rp5.000"
    )

    val textTestCase4 = "Belanja telur satu kerat 48.000 tuna 1/4 kilo 1513.000 dan tahu tempe Rp5.000"
    val nominalsTestCase4 = sequenceOf(
        "Rp5.000"
    )

    val text = "Ivan ngembaliin rp50.000 jadi hutangnya udah lunas"
    val nominals = sequenceOf("rp50.000")

    val result = descriptionExtractorNew(text, nominals)
    println(result)

    /* test case 2 expected result :
    // MutableList<Pair<String, String>> of
    // [
    //      (Bayar parkir, 2000 rupiah),
    //      (Bayar parkir, 2000 rupiah),
    // ]
    */

    println(descriptionExtractorNew(textTestCase1, nominalsTestCase1))
    println(descriptionExtractorNew(textTestCase2, nominalsTestCase2))
    println(descriptionExtractorNew(textTestCase3, nominalsTestCase3))
    println(descriptionExtractorNew(textTestCase4, nominalsTestCase4))
}

// todo :
//  val transactions = mutableListOf<Pair<String, String>>()
//  the string that hold nominal might null / empty
//  suggestion change type to mutableListOf<Pair<String, String?>>()
fun descriptionExtractor(text: String, nominals: Sequence<String>) : MutableList<Pair<String, String>> {
    // simpan text

    val processedNominals = mutableListOf<String>()
    val transactions = mutableListOf<Pair<String, String>>()

    for (nominal in nominals) {
        val startIdx = text.indexOf(nominal)

        val description = if (processedNominals.isEmpty()) {
            text.substring(0, startIdx).trim()
        } else {
            val prevNominal = processedNominals.last()
            val prevEndIdx = text.indexOf(prevNominal) + prevNominal.length
            text.substring(prevEndIdx, startIdx).trim()
        }

        transactions.add(description to nominal)
        processedNominals.add(nominal) // Track this nominal

        // hapus bagian text yang terdeteksi transaksi sebelumnya
    }

    return transactions
}

fun descriptionExtractorStepByStep(text: String, nominals: Sequence<String>) {
    val processedNominals = mutableListOf<String>() // To track previously seen nominals
    val transactions = mutableListOf<Pair<String, String>>()

    println("### Memulai proses ekstraksi transaksi ###\n")

    for (nominal in nominals) {
        println("Memproses nominal: \"$nominal\"")

        val startIdx = text.indexOf(nominal)
        val endIdx = startIdx + nominal.length

        println(" - Posisi awal nominal: $startIdx")
        println(" - Posisi akhir nominal: $endIdx")

        val description = if (processedNominals.isEmpty()) {
            println(" - Nominal pertama, mengambil deskripsi dari awal teks.")
            text.substring(0, startIdx).trim()
        } else {
            val prevNominal = processedNominals.last()
            val prevEndIdx = text.indexOf(prevNominal) + prevNominal.length

            println(" - Menggunakan nominal sebelumnya: \"$prevNominal\"")
            println(" - Posisi akhir nominal sebelumnya: $prevEndIdx")

            text.substring(prevEndIdx, startIdx).trim()
        }

        println(" - Deskripsi yang ditemukan: \"$description\"")

        transactions.add(description to nominal)
        println(" - Transaksi ditambahkan ke daftar.\n")

        processedNominals.add(nominal) // Track this nominal
    }

    println("### Hasil Akhir ###\n")
    for ((description, nominal) in transactions) {
        println("Description: \"$description\"")
        println("Nominal: \"$nominal\"")
        println()
    }

//    return transactions
}

fun descriptionExtractorNew(text: String, nominals: Sequence<String>): MutableList<Pair<String, String>> {
    var remainingText = text
    val transactions = mutableListOf<Pair<String, String>>()

    for (nominal in nominals) {
        val startIdx = remainingText.indexOf(nominal)

        if (startIdx == -1) continue

        val description = remainingText.substring(0, startIdx).trim()

        transactions.add(description to nominal)
        remainingText = remainingText.substring(startIdx + nominal.length).trim()
    }

    // Add the remaining text with a "0" nominal
    if (remainingText.isNotEmpty()) {
        transactions.add(remainingText to "0")
    }

    return transactions
}

fun descriptionExtractorWithDebug(text: String, nominals: Sequence<String>): MutableList<Pair<String, String>> {
    var remainingText = text
    val transactions = mutableListOf<Pair<String, String>>()

    println("Initial text: \"$text\"")
    println("Nominals: ${nominals.joinToString()}")

    for (nominal in nominals) {
        println("\nProcessing nominal: \"$nominal\"")
        val startIdx = remainingText.indexOf(nominal)

        if (startIdx == -1) {
            println("Nominal \"$nominal\" not found in remaining text: \"$remainingText\"")
            continue
        }

        // Deskripsi adalah teks sebelum nominal
        val description = remainingText.substring(0, startIdx).trim()
        println("Description: \"$description\"")

        // Tambahkan pasangan deskripsi dan nominal ke daftar
        transactions.add(description to nominal)
        println("Added pair: ($description, $nominal)")

        // Potong teks yang telah diproses, mulai dari akhir nominal saat ini
        remainingText = remainingText.substring(startIdx + nominal.length).trim()
        println("Remaining text after processing: \"$remainingText\"")
    }

    println("\nFinal transactions: $transactions")
    return transactions
}