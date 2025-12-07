package com.example.speechnancial.tools.algoritma


const val RP_LENGTH_MODIFIER = 2 // Panjang "rp"
const val RUPIAH_LENGTH_MODIFIER = 6 // Panjang "rupiah"
const val SPACING_LENGTH = 1 // Panjang spasi

fun prepareOccurrences(occurrences: Map<String, List<Int>>): List<Pair<Int, String>> {
    val occurrencesPair = mutableListOf<Pair<Int, String>>()
    occurrences.forEach { (key, indices) ->
        indices.forEach { index ->
            occurrencesPair.add(Pair(index, key))
        }
    }
    occurrencesPair.sortBy { it.first }
    println("Prepared and Sorted Occurrences: $occurrencesPair")
    return occurrencesPair
}

fun extractDescription(text: String, lastIndex: Int, currentIndex: Int, isRp: Boolean): String {
    val description = if (isRp) {
        println("Extracting description for 'rp' from $lastIndex to $currentIndex")
        text.substring(lastIndex, currentIndex).trim()
    } else {
        val descriptionEndIndex = currentIndex - (RUPIAH_LENGTH_MODIFIER + SPACING_LENGTH)
        println("Extracting description for 'rupiah' from $lastIndex to $descriptionEndIndex")
        try {
            text.substring(lastIndex, descriptionEndIndex).trim()
        } catch (e: StringIndexOutOfBoundsException) {
            ""
        }
    }
    println("Extracted Description: '$description'")
    return description
}

fun extractNominal(text: String, startIndex: Int, isRp: Boolean): String {
    val nominal = StringBuilder()
    var currentIndex = startIndex
    val condition: () -> Boolean
    val increment: () -> Unit
    val action: (Char) -> Unit

    when (isRp) {
        true -> {
            currentIndex = startIndex + RP_LENGTH_MODIFIER
            condition = { currentIndex < text.length }
            increment = { currentIndex++ }
            action = { nominal.append(it) }
        }
        false -> {
            currentIndex = startIndex - SPACING_LENGTH - 1
            condition = { currentIndex >= 0 }
            increment = { currentIndex-- }
            action = { nominal.insert(0, it) }
        }
    }

    println("Starting Nominal Extraction at index: $currentIndex (isRp: $isRp)")
    while (condition()) {
        val currentChar = text.getOrNull(currentIndex) ?: break
        println("Current Char at $currentIndex: '$currentChar'")
        if (currentChar.isDigit() || currentChar == '.') {
            action(currentChar)
        } else if (currentChar == ' ') {
            // Skip spaces within nominal
        } else {
            break // Stop if non-digit, non-space, non-dot encountered
        }
        increment()
    }
    println("Extracted Nominal: '$nominal'")
    return nominal.toString()
}

fun updateLastIndex(currentIndex: Int, key: String, nominalLength: Int): Int {
    val moveAfter = currentIndex + key.length + nominalLength
    println("Updating lastIndex to: $moveAfter (from current index: $currentIndex, key length: ${key.length}, nominal length: $nominalLength)")
    return moveAfter
}

fun nominalDescriptionSeparatorWithDebug(
    text: String = "pengeluaran parkir rp2000 pengeluaran sayur 5000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah",
    occurrences: Map<String, List<Int>> = mapOf(
        "rp" to listOf(19),
        "rupiah" to listOf(49, 113)
    )
): MutableList<Pair<String, String>> {
    println("Input Text Length: ${text.length}")
    println("Occurrences: $occurrences")

    val sortedOccurrences = prepareOccurrences(occurrences)
    val results = mutableListOf<Pair<String, String>>()
    var lastIndex = 0
    println("Initial lastIndex: $lastIndex")

    sortedOccurrences.forEach { (index, key) ->
        println("\n--- Processing index: $index, key: '$key' ---")
        val isRp = key == "rp"

        val description = extractDescription(text, lastIndex, index, isRp)

        val nominalStartIndex = if (isRp) index else index - (RUPIAH_LENGTH_MODIFIER + SPACING_LENGTH) + 1 // Start of nominal
        val nominal = extractNominal(text, nominalStartIndex, isRp)

        if (nominal.isNotEmpty()) {
            results.add(Pair(description, nominal))
            lastIndex = updateLastIndex(index, key, nominal.length)
        } else {
            println("Nominal is empty, skipping")
        }
        println("Current Results: $results")
        println("Current lastIndex: $lastIndex")
    }

    println("\n--- Final Results ---")
    println(results)
    return results
}

fun main() {
    nominalDescriptionSeparatorWithDebug()
}