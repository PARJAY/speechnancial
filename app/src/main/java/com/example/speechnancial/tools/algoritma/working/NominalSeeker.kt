package com.example.speechnancial.tools.algoritma.working

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
    text : String = "Pagi beli Laklak sama bikang Rp2.000 habis itu ke pasar Oh sebelum ke pasar beli risol Rp3.000 habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang 11.000 rupiah setelah itu ke Tohpati di sana dapat kelepon Rp5.000",
    occurrences: Map<String, List<Int>> = mapOf(
        "rp" to listOf(29, 87, 226),
        "rupiah" to listOf(174)
    )
) {
    val occurrencesPair = mutableListOf<Pair<Int, String>>()

    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            occurrencesPair.add(Pair(index, key))
        }
    }

    occurrencesPair.sortBy { it.first } // Urutkan berdasarkan indeks

    val results = mutableListOf<Pair<String, String>>() // Menyimpan Pair<Deskripsi, Nominal>
    var lastIndex = 0

    occurrencesPair.forEach { (index, key) ->
        val nominal = StringBuilder()
        val isRp = key == "rp"
        var currentIndex = if (isRp) index + RP_LENGTH_MODIFIER else index - 1

        val description = if (isRp) {
            text.substring(lastIndex, index).trim()
        } else {
            text.substring(lastIndex, index - (RUPIAH_LENGTH_MODIFIER + SPACING_LENGTH)).trim() // -6 untuk "rupiah"
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

    // Menghapus duplikat dan mencetak hasil
    results.distinct().forEach {
        println(it)
    }
    // Output: 2.000, 3.000, 5.000, 11000

    // output expected
    // Pagi beli Laklak sama bikang, 2.000
    // habis itu ke pasar Oh sebelum ke pasar beli risol, 3.000
    // habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang, 11.000
    // setelah itu ke Tohpati di sana dapat kelepon Rp5.000
}

fun main() {
    nominalDescriptionSeparator()
}