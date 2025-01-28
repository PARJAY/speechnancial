package com.example.speechnancial.tools.algoritma.working


//    print("rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah".length)

fun main() {
    val text = "Pagi beli Laklak sama bikang Rp2.000 habis itu ke pasar Oh sebelum ke pasar beli risol Rp3.000 habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang 11.000 rupiah setelah itu ke Tohpati di sana dapat kelepon Rp5.000"
    val patterns = listOf("rp", "rupiah")

    println("Text: \"$text\"")
    println("Patterns: $patterns\n")

    val occurrences = boyerMooreSearchMultiplePatternsWithDebug(text, patterns)

    if (occurrences.isEmpty()) {
        println("Tidak ada pola yang ditemukan.")
    } else {
        for ((pattern, indices) in occurrences) {
            println("$pattern ditemukan di indeks: $indices")
        }
    }
}

fun buildBadCharTableGPT(pattern: String): IntArray {
    val table = IntArray(256) { -1 } // ASCII size

    for (i in pattern.indices) {
        table[pattern[i].toInt()] = i
    }

    return table
}

fun boyerMooreSearchMultiplePatternsWithDebug(text: String, patterns: List<String>) : Map<String, List<Int>> {
    // Sort patterns by length (ascending)
    val sortedPatterns = patterns.sortedBy { it.length }
    println("Sorted Patterns: $sortedPatterns")

    // Build bad character table for the shortest pattern
    val shortestPattern = sortedPatterns.first()
    val badCharTable = buildBadCharTableGPT(shortestPattern)
    println("Bad Character Table for shortest pattern ('$shortestPattern'): $badCharTable\n")

    badCharTable.forEachIndexed { index, it ->
        if (it != -1) println("${index.toChar()} = $it")
    }

    val n = text.length
    val shortestPatternLength = shortestPattern.length - 1
    var shift = 0

    val patternAccumulationAndSkipMarkerList = mutableMapOf<String, Int>()
    sortedPatterns.forEach { patternAccumulationAndSkipMarkerList[it] = 0 }

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    while (shift <= n - (shortestPatternLength + 1)) {
        println("currently at shift $shift in text ${text[shift]}")
        var shifted = false

        for (pattern in sortedPatterns) {
            var j = pattern.length - 1

            // Compare pattern with text from right to left
            if (pattern == sortedPatterns[0]) {
                while (j >= 0 && pattern[j] == text[shift + j].lowercaseChar()) {
                    println("Pattern ${pattern[j]} Match with ${text[shift + j]}")
                    j--
                }
            }
            else if (shift + pattern.length <= text.length && pattern[patternAccumulationAndSkipMarkerList[pattern]!! + shortestPatternLength] == text[shift + j]) {
                println("Pattern $pattern [accumulation + shortestPatternLength] (${pattern[patternAccumulationAndSkipMarkerList[pattern]!! + shortestPatternLength]}) Match with ${text[shift + j]}")
                patternAccumulationAndSkipMarkerList[pattern] =
                    (patternAccumulationAndSkipMarkerList[pattern] ?: 0) + shortestPatternLength + 1

                if (patternAccumulationAndSkipMarkerList[pattern]!! >= pattern.length) {
                    patternAccumulationAndSkipMarkerList[pattern]!! - 1
                    println("Fully accumulation reached for pattern '$pattern' at index $shift")
                }
                else {
                    println(
                        "patternAccumulationAndSkipMarkerList[pattern] = ${patternAccumulationAndSkipMarkerList[pattern]} " +
                        "==> Pattern $pattern [accumulation + shortestPatternLength] (${pattern[patternAccumulationAndSkipMarkerList[pattern]!! + shortestPatternLength]})"
                    )
                }
            } else {
                try {
                    println("Pattern $pattern [accumulation + shortestPatternLength] (${pattern[patternAccumulationAndSkipMarkerList[pattern]!! + shortestPatternLength]}) Not Match with ${text[shift + j]}")
                } catch (e : Exception) {
                    println("Pattern out of range")
                }
                patternAccumulationAndSkipMarkerList[pattern] = 0
            }

            // Check for full accumulation for patternAccumulationAndSkipMarkerList key - value and pattern.length
            if (patternAccumulationAndSkipMarkerList[pattern]!! >= pattern.length) {
                j -= 1
                while (j >= 0 && pattern[j] == text[shift + j]) {
                    println("Pattern ${pattern[j]} Match with ${text[shift + j]}")
                    j -= shortestPatternLength + 1
                }

                println("Full accumulation reached for pattern '$pattern' at index $shift")
                println("Reseting accumulation to 0")
                patternAccumulationAndSkipMarkerList[pattern] = 0
            }

            if (j < 0) { // Full match for this pattern
                occurrences[pattern]!!.add(shift)
                println("Pattern '$pattern' found at index $shift")
            }
            else if(pattern == sortedPatterns[0]) {
                val badCharShift = j - badCharTable[text[shift + j].lowercaseChar().code]
                println("val badCharShift $badCharShift = j $j - badCharTable[text[shift + j].toInt()] ${badCharTable[text[shift+j].lowercaseChar().code]}")
                println("first pattern Mismatch at index ${shift + j} (character: '${text[shift + j]}')")
                println("Shifting pattern by max(1, $badCharShift)\n")
                shift += maxOf(1, badCharShift)
                shifted = true
            }
            else {
                try {
                    println("Mismatch for pattern '$pattern' at index ${shift + j} (character: ${pattern[j]} != '${text[shift + j]}')")
                } catch (e : Exception) {
                    println("Pattern out of range")
                }
            }
        }

        // Shift based on shortest pattern
        if (!shifted && shift + shortestPatternLength < n) {
            val badCharShift = shortestPatternLength - badCharTable[text[shift + shortestPatternLength].toInt()]
            println("Shifting by max(1, $badCharShift)\n")
            shift += maxOf(1, badCharShift)
        }
//        else {
//            shift++
//        }
    }

    return occurrences
}


fun boyerMooreSearchMultiplePatterns(text: String, patterns: List<String>) : Map<String, List<Int>> {
    // Sort patterns by length (ascending)
    val sortedPatterns = patterns.sortedBy { it.length }

    // Build bad character table for the shortest pattern
    val shortestPattern = sortedPatterns.first()
    val badCharTable = buildBadCharTableGPT(shortestPattern)

    val n = text.length
    val shortestPatternLength = shortestPattern.length - 1
    var shift = 0

    val patternAccumulationAndSkipMarkerList = mutableMapOf<String, Int>()
    sortedPatterns.forEach { patternAccumulationAndSkipMarkerList[it] = 0 }

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    while (shift <= n - (shortestPatternLength + 1)) {
        var shifted = false
        for (pattern in sortedPatterns) {
            var j = pattern.length - 1

            if (patternAccumulationAndSkipMarkerList[pattern] == -1) {
                continue
            }

            // Compare pattern with text from right to left
            if (pattern == sortedPatterns[0]) {
                while (j >= 0 && pattern[j] == text[shift + j].lowercaseChar()) {
                    j--
                }
            }
            else if (shift + pattern.length <= text.length && pattern[patternAccumulationAndSkipMarkerList[pattern]!! + shortestPatternLength] == text[shift + j]) {
                patternAccumulationAndSkipMarkerList[pattern] =
                    (patternAccumulationAndSkipMarkerList[pattern] ?: 0) + shortestPatternLength + 1

                if (patternAccumulationAndSkipMarkerList[pattern]!! >= pattern.length) {
                    patternAccumulationAndSkipMarkerList[pattern]!! - 1
                }
            } else {
                patternAccumulationAndSkipMarkerList[pattern] = 0
            }

            // Check for full accumulation for patternAccumulationAndSkipMarkerList key - value and pattern.length
            if (patternAccumulationAndSkipMarkerList[pattern]!! >= pattern.length) {
                j -= 1
                while (j >= 0 && pattern[j] == text[shift + j]) {
                    j -= shortestPatternLength + 1
                }
                patternAccumulationAndSkipMarkerList[pattern] = 0
            }

            if (j < 0) { // Full match for this pattern
                occurrences[pattern]!!.add(shift)
            }
            else if(pattern == sortedPatterns[0]) {
                val badCharShift = j - badCharTable[text[shift + j].lowercaseChar().code]
                shift += maxOf(1, badCharShift)
                shifted = true
            }
        }

        // Shift based on shortest pattern
        if (!shifted && shift + shortestPatternLength < n) {
            val badCharShift = shortestPatternLength - badCharTable[text[shift + shortestPatternLength].toInt()]
            shift += maxOf(1, badCharShift)
        }
    }

    return occurrences
}
