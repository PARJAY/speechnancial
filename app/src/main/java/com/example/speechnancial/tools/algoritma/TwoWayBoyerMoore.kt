package com.example.speechnancial.tools.algoritma

import kotlin.math.max

// Fungsi untuk membuat tabel Bad Character untuk pencarian dari depan
fun buatTabelBadCharDepan(pattern: String): HashMap<Char, Int> {
    val tabel = HashMap<Char, Int>()
    for (i in pattern.indices) {
        tabel[pattern[i]] = i
    }
    return tabel
}

// Fungsi untuk membuat tabel Bad Character untuk pencarian dari belakang
fun buatTabelBadCharBelakang(pattern: String): HashMap<Char, Int> {
    val tabel = HashMap<Char, Int>()
    for (i in pattern.length - 1 downTo 0) {
        tabel[pattern[i]] = pattern.length - 1 - i
    }
    return tabel
}

// Fungsi Boyer-Moore untuk pencarian dari depan
fun boyerMooreDepan(text: String, pattern: String): Int {
    val n = text.length
    val m = pattern.length

    if (m == 0) return 0

    val badChar = buatTabelBadCharDepan(pattern)
    var s = 0 // s adalah pergeseran pattern relatif terhadap teks
    while (s <= n - m) {
        var j = 0
        while (j < m && pattern[j] == text[s + j]) {
            j++
        }
        if (j == m) return s // Ditemukan
        s += max(1, j - (badChar[text[s + j]] ?: -1))
    }
    return -1 // Tidak ditemukan
}

// Fungsi Boyer-Moore untuk pencarian dari belakang
fun boyerMooreBelakang(text: String, pattern: String): Int {
    val n = text.length
    val m = pattern.length

    if (m == 0) return 0

    val badChar = buatTabelBadCharBelakang(pattern)
    var s = n - m
    while (s >= 0) {
        var j = m - 1
        while (j >= 0 && pattern[j] == text[s + j]) {
            j--
        }
        if (j == -1) return s // Ditemukan
        s -= max(1, (badChar[text[s + j]] ?: -1) - (m - 1 - j))
    }
    return -1 // Tidak ditemukan
}

// Fungsi Boyer-Moore Dua Arah
fun boyerMooreDuaArah(text: String, pattern: String): Int {
    val n = text.length
    val m = pattern.length

    if (m == 0) return 0

    var indeksDepan = 0
    var indeksBelakang = n - 1

    while (indeksDepan <= indeksBelakang) {
        val cocokDepan = boyerMooreDepan(text.substring(indeksDepan), pattern)
        val cocokBelakang = boyerMooreBelakang(text.substring(0, indeksBelakang + 1), pattern)

        if (cocokDepan != -1 && cocokBelakang != -1) {
            if (indeksDepan + cocokDepan + m - 1 >= indeksBelakang - (m - 1 - cocokBelakang)) {
                return indeksDepan + cocokDepan // Ditemukan
            }
        }

        if (cocokDepan != -1) {
            indeksDepan += cocokDepan + 1
        }
        if (cocokBelakang != -1) {
            indeksBelakang -= (m - 1 - cocokBelakang) + 1
        }

    }
    return -1 // Tidak ditemukan
}

fun main() {
    val text = "memasak makanan enak sekali"
    val pattern = "makan"
    val hasil = boyerMooreDuaArah(text, pattern)

    if (hasil != -1) {
        println("Pattern ditemukan di indeks: $hasil")
    } else {
        println("Pattern tidak ditemukan")
    }

//    val text2 = "ini bukan makanan"
//    val pattern2 = "makan"
//    val hasil2 = boyerMooreDuaArah(text2, pattern2)
//    if (hasil2 != -1) {
//        println("Pattern ditemukan di indeks: $hasil2")
//    } else {
//        println("Pattern tidak ditemukan")
//    }
//
//    val text3 = "makanmakanmakan"
//    val pattern3 = "makan"
//    val hasil3 = boyerMooreDuaArah(text3, pattern3)
//    if (hasil3 != -1) {
//        println("Pattern ditemukan di indeks: $hasil3")
//    } else {
//        println("Pattern tidak ditemukan")
//    }
}