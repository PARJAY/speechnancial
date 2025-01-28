package com.example.speechnancial.tools.algoritma

import kotlin.math.max

fun boyerMooreMultiplePatternsWithReturn(text: String, patterns: List<String>): Map<String, List<Int>> {
    val textLength = text.length
    if (textLength == 0 || patterns.isEmpty()) return emptyMap()

    val sortedPatterns = patterns.sortedBy { it.length }

    // Preprocessing: Membuat tabel bad character untuk semua pola
    val newBadCharTable = mutableMapOf<Char, Int>()

    val badCharTable = IntArray(256) { -1 }
    for (pattern in sortedPatterns) {
        for (patternChar in pattern.indices) {
            badCharTable[pattern[patternChar].code] = patternChar
            newBadCharTable[pattern[patternChar]] = patternChar
        }
    }

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    var shift = 0
    while (shift <= textLength - 1) {
        var shifted = false
        for (pattern in patterns) {
            val patternLength = pattern.length
            if (shift + patternLength > textLength) continue

            var j = patternLength - 1
            while (j >= 0 && pattern[j] == text[shift + j]) {
                j--
            }

            if (j < 0) {
                occurrences[pattern]!!.add(shift)
                shift++ // Penting: Geser satu karakter untuk mencegah overlap
                shifted = true
                break // Keluar dari loop pola setelah menemukan satu pola
            }
        }
        if (!shifted) { // Jika tidak ada pola yang ditemukan pada shift saat ini
            var shiftAmount = 1
            if (shift < textLength) {
                val badCharIndex = text[shift].code
                val badCharPos = badCharTable[badCharIndex]
                shiftAmount = max(1, 0 - badCharPos)
            }
            shift += shiftAmount
        }
    }

    return occurrences
}

fun boyerMooreMultiplePatternsWithReturnAndDebug(text: String, patterns: List<String>): Map<String, List<Int>> {
    val textLength = text.length
    if (textLength == 0 || patterns.isEmpty()) return emptyMap()

    println("Text: \"$text\"")
    println("Patterns: ${patterns.joinToString(", ")}")

    val sortedPatterns = patterns.sortedBy { it.length }
    println("Sorted Patterns: ${sortedPatterns.joinToString(", ")}")

    // Preprocessing: Membuat tabel bad character untuk semua pola
    val badCharTable = IntArray(256) { -1 }
    for (pattern in sortedPatterns) {
        for (patternChar in pattern.indices) {
            badCharTable[pattern[patternChar].code] = patternChar
        }
    }
    println("Bad Character Table: ${badCharTable.joinToString(", ")}")

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    var shift = 0
    while (shift <= textLength - 1) {
        println("\nShift: $shift")
        var shifted = false
        // modify here

        for (pattern in patterns) {
            val patternLength = pattern.length
            if (shift + patternLength > textLength) {
                println("Skipping pattern \"$pattern\" (out of bounds)")
                continue
            }

            println("Checking pattern \"$pattern\" at position $shift")

            var j = patternLength - 1
            while (j >= 0 && pattern[j] == text[shift + j]) {
                println("Match at text[${shift + j}] == pattern[$j] ('${text[shift + j]}')")
                j--
            }

            if (j < 0) {
                println("Pattern \"$pattern\" found at index $shift")
                occurrences[pattern]!!.add(shift)
                shift++ // Geser satu karakter untuk mencegah overlap
                shifted = true
                break // Keluar dari loop pola setelah menemukan satu pola
            } else {
                println("Mismatch at text[${shift + j}] != pattern[$j] ('${text[shift + j]}' vs '${pattern[j]}')")
            }
        }

        if (!shifted) { // Jika tidak ada pola yang ditemukan pada shift saat ini
            var shiftAmount = 1
            if (shift < textLength) {
                val badCharIndex = text[shift].code
                val badCharPos = badCharTable[badCharIndex]
                shiftAmount = max(1, 0 - badCharPos)
                println("Bad character '${text[shift]}' at index $shift, shift by $shiftAmount")
            }
            shift += shiftAmount
        }
    }

    println("\nOccurrences:")
    occurrences.forEach { (pattern, indices) ->
        println("Pattern \"$pattern\" found at indices ${indices.joinToString(", ")}")
    }

    return occurrences
}


fun boyerMooreMultiplePatternsWithReturnAndDebugOptimized(text: String, patterns: List<String>): Map<String, List<Int>> {
    val textLength = text.length
    if (textLength == 0 || patterns.isEmpty()) return emptyMap()

    println("Text: \"$text\"")
    println("Patterns: ${patterns.joinToString(", ")}")

    val sortedPatterns = patterns.sortedBy { it.length }
    println("Sorted Patterns: ${sortedPatterns.joinToString(", ")}")

    // Preprocessing: Membuat tabel bad character untuk semua pola
    val newBadCharTable = mutableMapOf<Char, Int>()
    val badCharTable = IntArray(256) { -1 }
    for (pattern in sortedPatterns) {
        for (patternCharIndex in pattern.indices) {
            badCharTable[pattern[patternCharIndex].code] = patternCharIndex
            newBadCharTable[pattern[patternCharIndex]] = patternCharIndex
        }
    }
    println("Bad Character Table: ${badCharTable.joinToString(", ")}")

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    var shift = 0
    while (shift <= textLength - 1) {
        println("\nShift: $shift")
        var shifted = false

        // modify here
        val shortestPatternLength = patterns[0].length
        println("shortestPatternLength : $shortestPatternLength")

        if (shift + shortestPatternLength > textLength) {
            println("Shortest pattern \"$patterns[0]\" is out of bounds")
        }

//        var j = shortestPatternLength - 1
//        while (j >= 0 && pattern[j] == text[shift + j]) {
//            println("Match at text[${shift + j}] == pattern[$j] ('${text[shift + j]}')")
//            j--
//        }

        for (pattern in patterns.indices) {
            var j = patterns[pattern].length - 1
            println(patterns[pattern][j])
        }

        for (pattern in patterns) {
            val patternLength = pattern.length
            if (shift + patternLength > textLength) {
                println("Skipping pattern \"$pattern\" (out of bounds)")
                continue
            }

            println("Checking pattern \"$pattern\" at position $shift")

            var j = patternLength - 1
            while (j >= 0 && pattern[j] == text[shift + j]) {
                println("Match at text[${shift + j}] == pattern[$j] ('${text[shift + j]}')")
                j--
            }

            if (j < 0) {
                println("Pattern \"$pattern\" found at index $shift")
                occurrences[pattern]!!.add(shift)
                shift++ // Geser satu karakter untuk mencegah overlap
                shifted = true
                break // Keluar dari loop pola setelah menemukan satu pola
            } else {
                println("Mismatch at text[${shift + j}] != pattern[$j] ('${text[shift + j]}' vs '${pattern[j]}')")
            }
        }

        if (!shifted) { // Jika tidak ada pola yang ditemukan pada shift saat ini
            var shiftAmount = 1
            if (shift < textLength) {
                val badCharIndex = text[shift].code
                val badCharPos = badCharTable[badCharIndex]
                shiftAmount = max(1, 0 - badCharPos)
                println("Bad character '${text[shift]}' at index $shift, shift by $shiftAmount")
            }
            shift += shiftAmount
        }
    }

    println("\nOccurrences:")
    occurrences.forEach { (pattern, indices) ->
        println("Pattern \"$pattern\" found at indices ${indices.joinToString(", ")}")
    }

    return occurrences
}

fun main() {
    val text = "rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah"
    val patterns = listOf("rp", "rupiah")
    val occurrences = boyerMooreMultiplePatternsWithReturnAndDebugOptimized(text, patterns)

    if (occurrences.isEmpty()) {
        println("Tidak ada pola yang ditemukan.")
    } else {
        for ((pattern, indices) in occurrences) {
            println("$pattern ditemukan di indeks: $indices")
        }
    }
}