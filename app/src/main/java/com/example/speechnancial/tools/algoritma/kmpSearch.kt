package com.example.speechnancial.tools.algoritma

// pattern start with "rp"
// after "rp" can be followed by "." and " " but is optional

// nominal pattern
// after that can be number 0 - 9
// can be followed by "." and after that must be followed by 3 number (repeatable)
// can be followed by "," and after that must be followed by number only (not repeatable)
// can be followed with angka digit

fun kmpSearch(text: String, pattern: String): List<Int> {
    val n = text.length
    val m = pattern.length

    if (m == 0) {
        return emptyList() // Pattern kosong, tidak ada kecocokan
    }

    val lps = computeLPSArray(pattern) // Menghitung tabel LPS

    val occurrences = mutableListOf<Int>()
    var i = 0 // Index untuk teks
    var j = 0 // Index untuk pattern

    while (i < n) {
        if (pattern[j] == text[i]) {
            i++
            j++
        }

        if (j == m) {
            occurrences.add(i - j) // Kecocokan ditemukan
            j = lps[j - 1] // Reset j menggunakan LPS untuk mencari kecocokan berikutnya
        } else if (i < n && pattern[j] != text[i]) { // Ketidakcocokan setelah beberapa karakter cocok
            if (j != 0) {
                j = lps[j - 1] // Reset j menggunakan LPS
            } else {
                i++ // Jika j == 0, geser teks
            }
        }
    }

    return occurrences
}

fun computeLPSArray(pattern: String): IntArray {
    val m = pattern.length
    val lps = IntArray(m)
    var len = 0 // Panjang awalan terpanjang yang juga merupakan akhiran
    lps[0] = 0 // LPS untuk karakter pertama selalu 0
    var i = 1

    // Menghitung tabel LPS
    while (i < m) {
        if (pattern[i] == pattern[len]) {
            len++
            lps[i] = len
            i++
        } else { // (pattern[i] != pattern[len])
            if (len != 0) {
                len = lps[len - 1] // Ini krusial untuk KMP
                // Perhatikan bahwa kita tidak menginkrementasi i di sini
            } else { // Jika len == 0
                lps[i] = len
                i++
            }
        }
    }
    return lps
}

fun main() {
//    sampleUsage()

    val text = "Ack rp32.500"
    val pattern = "rp"
    val occurrences = kmpSearch(text, pattern)
    println("Pattern ditemukan pada indeks: $occurrences") // Output: [4]

    // setelah menemukan rp, ganti pattern menjadi angka dan lanjutkan indeks

    val patternAngka = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0)      // is number || "." aja langsung
    val daftarDigitDalamKata = listOf(" puluh "," ratus "," ribu "," juta "," miliar "," triliun ")

}

fun sampleUsage() {
    val text = "ABABDABACDABABCABAB"
    val pattern = "ABABCABAB"
    val occurrences = kmpSearch(text, pattern)

    if (occurrences.isEmpty()) {
        println("Pattern tidak ditemukan dalam teks.")
    } else {
        println("Pattern ditemukan pada indeks: $occurrences") // Output: [10]
    }

    val text2 = "AAAAABAAABA"
    val pattern2 = "AAAA"
    val occurrences2 = kmpSearch(text2, pattern2)
    println("Pattern ditemukan pada indeks: $occurrences2") // Output: [0, 1]

    val text3 = "ABC ABCDAB ABCDABCDABDE"
    val pattern3 = "ABCDABD"
    val occurrences3 = kmpSearch(text3, pattern3)
    println("Pattern ditemukan pada indeks: $occurrences3") // Output: [15]

    val text4 = "aabaacaadaabaabaaa"
    val pattern4 = "aabaabaaa"
    val occurrences4 = kmpSearch(text4, pattern4)
    println("Pattern ditemukan pada indeks: $occurrences4") // Output: [9]

    val text5 = "geeksforgeeks"
    val pattern5 = "geeks"
    val occurrences5 = kmpSearch(text5, pattern5)
    println("Pattern ditemukan pada indeks: $occurrences5") // Output: [0]
}
