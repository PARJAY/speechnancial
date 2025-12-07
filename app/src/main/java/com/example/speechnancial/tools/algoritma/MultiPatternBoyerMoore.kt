package com.example.speechnancial.tools.algoritma

import kotlin.math.max

fun boyerMooreMultiplePatterns(text: String, patterns: List<String>): List<Int> {
    val textLength = text.length
    if (textLength == 0 || patterns.isEmpty()) return emptyList()

    // 1. Preprocessing: Membuat tabel bad character untuk semua pola
    val badCharTable = IntArray(256) { -1 }
    var maxPatternLength = 0
    for (pattern in patterns) {
        maxPatternLength = max(maxPatternLength, pattern.length)
        for (i in pattern.indices) {
            badCharTable[pattern[i].code] = i
        }
    }

    for (i in badCharTable) {
        if (i != -1) println("$i")
    }

    val occurrences = mutableListOf<Int>()
    var shift = 0
    while (shift <= textLength - 1) { // Perubahan penting disini
        var found = false
        for (pattern in patterns) {
            val patternLength = pattern.length
            if (shift + patternLength > textLength) continue // Cek batas teks

            var j = patternLength - 1
            while (j >= 0 && pattern[j] == text[shift + j]) {
                j--
            }

            if (j < 0) {
                occurrences.add(shift)
                found = true
                break // Keluar dari loop pola jika sudah ditemukan
            }
        }
        if (found) {
            shift++ // Geser 1 jika pola ditemukan untuk menghindari overlap
        } else {
            // 2. Menghitung pergeseran
            var shiftAmount = 1
            if (shift < textLength) { // Pastikan tidak melebihi batas teks
                val badCharIndex = text[shift].code
                val badCharPos = badCharTable[badCharIndex]
                shiftAmount = max(1, 0 - badCharPos) // Perubahan penting disini
            }
            shift += shiftAmount
        }
    }
    return occurrences
}

fun main() {
//    val text = "rpaah teks rupiah untuk mencari rp dan rupiah"
    val text = "pengeluaran parkir rp2000 pengeluaran 5000 rupiah pemasukan ketemu paman di pasar dan dibekelin uang 50000 rupiah"
    val patterns = listOf("rp", "rupiah")
    val occurrences = boyerMooreMultiplePatterns(text, patterns)

    if (occurrences.isEmpty()) {
        println("Tidak ada pola yang ditemukan.")
    } else {
        println("Pola ditemukan pada indeks: $occurrences")
    }

}