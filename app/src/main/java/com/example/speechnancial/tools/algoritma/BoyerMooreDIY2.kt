package com.example.speechnancial.tools.algoritma

import kotlin.math.max

//fun boyerMooreDIY2(text: String, patterns: List<String>): Map<String, List<Int>> {
//    val textLength = text.length
//    if (textLength == 0 || patterns.isEmpty()) return emptyMap()
//
//    println("Text: \"$text\"")
//    println("Patterns: ${patterns.joinToString(", ")}")
//
//    val sortedPatterns = patterns.sortedBy { it.length }
//    println("Sorted Patterns: ${sortedPatterns.joinToString(", ")}")
//
//    val badCharTable = mutableMapOf<Char, Int>()
//
//    // hanya pattern terpendek saja
//    for (shortestPattern in sortedPatterns[0]) {
//        badCharTable[shortestPattern] = shortestPattern.code
//    }
//
//    println("Bad Character Table: $badCharTable")
//
//    val occurrences = mutableMapOf<String, MutableList<Int>>()
//    for (pattern in patterns) {
//        occurrences[pattern] = mutableListOf()
//    }
//
//
//    val shortestPatternLength = patterns[0].length - 1
//    println("shortestPatternLength : $shortestPatternLength")
//
//    var textShift = 0
//    var accumulation = 0
//
//    val patternSkipMarkerList = mutableListOf<String>()
//
//    while (textShift <= textLength - 1) {
//        textShift += shortestPatternLength
//    }
//
//    var shift = 0
//    while (shift <= textLength - 1) {
//        println("\nShift: $shift")
//        var shifted = false
//
//        for (pattern in patterns) {
//            val patternLength = pattern.length
//            if (shift + patternLength > textLength) {
//                println("Skipping pattern \"$pattern\" (out of bounds)")
//                continue
//            }
//
//            println("Checking pattern \"$pattern\" at position $shift")
//
//            var j = patternLength - 1
//            while (j >= 0 && pattern[j] == text[shift + j]) {
//                println("Match at text[${shift + j}] == pattern[$j] ('${text[shift + j]}')")
//                j--
//            }
//
//            if (j < 0) {
//                println("Pattern \"$pattern\" found at index $shift")
//                occurrences[pattern]!!.add(shift)
//                shift++ // Geser satu karakter untuk mencegah overlap
//                shifted = true
//                break // Keluar dari loop pola setelah menemukan satu pola
//            } else {
//                println("Mismatch at text[${shift + j}] != pattern[$j] ('${text[shift + j]}' vs '${pattern[j]}')")
//            }
//        }
//
//        if (!shifted) { // Jika tidak ada pola yang ditemukan pada shift saat ini
//            var shiftAmount = 1
//            if (shift < textLength) {
//                val badCharIndex = text[shift].code
//                val badCharPos = badCharTable[badCharIndex]
//                shiftAmount = max(1, 0 - badCharPos)
//                println("Bad character '${text[shift]}' at index $shift, shift by $shiftAmount")
//            }
//            shift += shiftAmount
//        }
//    }
//
//    println("\nOccurrences:")
//    occurrences.forEach { (pattern, indices) ->
//        println("Pattern \"$pattern\" found at indices ${indices.joinToString(", ")}")
//    }
//
//    return occurrences
//}
