package com.example.speechnancial.tools.algoritma

import kotlin.math.max

fun boyerMoore(text: String, pattern: String) {
    val n = text.length
    val m = pattern.length

    if (m == 0) {
        println("Pola kosong.")
        return
    }

    if (m > n) {
        println("Pola lebih panjang dari teks.")
        return
    }

    val badChar = badCharacterTable(pattern)
    val goodSuffix = goodSuffixTable(pattern)

    println("Bad Character Table: ${badChar.toList()}")
    println("Good Suffix Table: ${goodSuffix.toList()}")

    var s = 0 // s adalah pergeseran pola relatif terhadap teks
    while (s <= n - m) {
        println("Pergeseran: $s")
        var j = m - 1

        while (j >= 0 && pattern[j] == text[s + j]) {
            println("Pencocokan: text[${s + j}] (${text[s + j]}) == pattern[$j] (${pattern[j]})")
            j--
        }

        if (j < 0) {
            println("Pola ditemukan pada indeks: $s")
            s += if (s + m < n) m - goodSuffix[0] else 1 // Pergeseran good suffix jika memungkinkan, jika tidak geser 1
        } else {
            println("Ketidakcocokan pada: text[${s + j}] (${text[s + j]}) != pattern[$j] (${pattern[j]})")
            val badCharShift = max(1, j - (badChar[text[s + j].toInt()] ?: -1))
            val goodSuffixShift = if (j < m - 1) goodSuffix[j + 1] else 1
            s += max(badCharShift, goodSuffixShift)
            println("Pergeseran bad character: $badCharShift")
            println("Pergeseran good suffix: $goodSuffixShift")

        }
        println("--------------------")
    }
}

fun badCharacterTable(pattern: String): Map<Int, Int> {
    val m = pattern.length
    val badChar = mutableMapOf<Int, Int>()

    println("Membuat Tabel Bad Character untuk pattern: '$pattern'")
    for (i in 0 until m) {
        val charInt = pattern[i].toInt()
        println("Karakter: '${pattern[i]}' (ASCII: $charInt), Indeks: $i")
        badChar[charInt] = i
        println("Tabel Bad Character saat ini: $badChar")
    }
    println("Tabel Bad Character Selesai: $badChar\n")
    return badChar
}

fun goodSuffixTable(pattern: String): IntArray {
    val m = pattern.length
    val goodSuffix = IntArray(m + 1)
    val lastPrefixPosition = IntArray(m + 1)

    println("Membuat Tabel Good Suffix untuk pattern: '$pattern'")
    println("Inisialisasi lastPrefixPosition: ${lastPrefixPosition.contentToString()}")

    for (i in m downTo 0) {
        lastPrefixPosition[i] = if (isPrefix(pattern, pattern.substring(i))) i else lastPrefixPosition[i + 1]
        println("lastPrefixPosition[$i] = ${lastPrefixPosition[i]}, (substring: '${pattern.substring(i)}', isPrefix: ${isPrefix(pattern, pattern.substring(i))})")
    }
    println("lastPrefixPosition setelah loop pertama: ${lastPrefixPosition.contentToString()}")

    var j = 0
    for (i in 0 until m) {
        if (i + 1 < m && lastPrefixPosition[i + 1] < i + 1) {
            goodSuffix[i] = m - 1 - i
            println("goodSuffix[$i] = $m - 1 - $i = ${goodSuffix[i]} (Kondisi 1 terpenuhi)")
        } else {
            goodSuffix[i] = m - lastPrefixPosition[0]
            println("goodSuffix[$i] = $m - ${lastPrefixPosition[0]} = ${goodSuffix[i]} (Kondisi 2 terpenuhi)")
        }
    }
    println("Tabel Good Suffix Selesai: ${goodSuffix.contentToString()}\n")
    return goodSuffix
}

fun isPrefix(pattern: String, subPattern: String): Boolean {
    val subPatternLength = subPattern.length
    if (subPatternLength > pattern.length) return false
    for (i in 0 until subPatternLength) {
        if (pattern[i] != subPattern[i]) return false
    }
    return true
}

fun main() {
//    val text = "GCATCGCAGAGAGTATATCAGA"
//    val pattern = "GCAGAGAG"
//    boyerMoore(text, pattern)

    println("\nContoh 2:")
    val text2 = "ABABDABACDABABCABAB"
    val pattern2 = "ABABCABAB"
    boyerMoore(text2, pattern2)
//
//    println("\nContoh 3 (Pola lebih panjang dari teks):")
//    val text3 = "ABC"
//    val pattern3 = "ABCD"
//    boyerMoore(text3, pattern3)
//
//    println("\nContoh 4 (Pola kosong):")
//    val text4 = "ABC"
//    val pattern4 = ""
//    boyerMoore(text4, pattern4)
}