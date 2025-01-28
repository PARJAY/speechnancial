package com.example.speechnancial.tools.algoritma
//
//fun boyerMooreFresh(text: String, pattern: String) {
//    val badCharTable = buildBadCharTableFresh(pattern)
//    println("Bad Character Table: $badCharTable\n")
//
//    val n = text.length
//    val m = pattern.length
//    var shift = 0
//
//    while (shift <= n - m) {
//        var j = m - 1
//
//        // Compare pattern with text from right to left
//        while (j >= 0 && pattern[j] == text[shift + j]) {
//            j--
//        }
//
//        if (j < 0) {
//            println("Pattern found at index $shift")
//
//            // Shift pattern so next character in text aligns with the last occurrence in pattern
//            shift += if (shift + m < n) m - badCharTable[text[shift + m].toInt()] else 1
//        } else {
//            // Shift pattern to align the bad character
//            val badCharShift = j - badCharTable[text[shift + j].toInt()]
//            println("Mismatch at index ${shift + j} (character: '${text[shift + j]}')")
//            println("Shifting pattern by max(1, $badCharShift)\n")
//            shift += maxOf(1, badCharShift)
//        }
//    }
//}
//
//fun main() {
//    val text = "THIS IS A SIMPLE EXAMPLE"
//    val pattern = "EXAMPLE"
//
//    println("Text: \"$text\"")
//    println("Pattern: \"$pattern\"\n")
//
//    boyerMooreFresh(text, pattern)
//}
//
//fun buildBadCharTableFresh(pattern: String): IntArray {
//    val newBadCharTable = mutableMapOf<Char, Int>()
//
//    // hanya pattern terpendek saja
//    for (character in pattern) {
//        newBadCharTable[character] = character.code
//    }
//
//    val table = IntArray(256) { -1 } // ASCII size
//
//    for (i in pattern.indices) {
//        table[pattern[i].toInt()] = i
//    }
//
//    return table
//}