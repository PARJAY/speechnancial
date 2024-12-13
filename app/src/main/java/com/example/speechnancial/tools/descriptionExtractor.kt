package com.example.speechnancial.tools

fun main() {
    val text =
        "Beli ESP 32 rp100 miliar 100 juta 100.000,000 dht rp 25.000 kabel jumper rp. 45.000.000.000.000 breadboard 400 poin rp dua puluh delapan ribu" +
        "cuci uang korupsi bambang tambang timah rp 12 miliar 11 juta 19.111 cuci uang korupsi bambang tambang timah 12 miliar 11 juta 19.111 rupiah"

    val nominals = sequenceOf(
        "rp100 miliar 100 juta 100.000,000",
        "rp 25.000",
        "rp. 45.000.000.000.000",
        "rp 12 miliar 11 juta 19.111",
        "12 miliar 11 juta 19.111 rupiah"
    )
    descriptionExtractor(text, nominals)
}

// todo :
//  val transactions = mutableListOf<Pair<String, String>>()
//  the string that hold nominal might null / empty
//  suggestion change type to mutableListOf<Pair<String, String?>>()
fun descriptionExtractor(text: String, nominals: Sequence<String>) : MutableList<Pair<String, String>> {
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