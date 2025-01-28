package com.example.speechnancial.tools.algoritma

import kotlin.math.max

//function boyerMoore(teks, pola):

// patterns: List<String> = listOf("rp", "rupiah")
//{r=0, p=2, u=1, i=3, a=4, h=5}
//p => 2

// patterns: List<String> = listOf("rupiah", "rp")
//{r=0, p=1, u=1, i=3, a=4, h=5}
// p = 1

fun boyerMooreDiy(
    text: String = "rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah",
    patterns: List<String> = listOf("rp", "rupiah")
): Map<String, List<Int>> {
    val textLength = text.length
    if (text.isEmpty() || patterns.isEmpty()) return emptyMap()

    val sortedPatterns = patterns.sortedBy { it.length }

    val newBadCharTable = mutableMapOf<Char, Int>()

    // hanya pattern terpendek saja
    for (shortestPattern in sortedPatterns[0]) {
        newBadCharTable[shortestPattern] = shortestPattern.code
    }

    val occurrences = mutableMapOf<String, MutableList<Int>>()
    for (pattern in patterns) {
        occurrences[pattern] = mutableListOf()
    }

    val shortestPatternLength = patterns[0].length - 1
    println("shortestPatternLength : $shortestPatternLength")

    var textShift = 0
    var accumulation = 0

    val patternSkipMarkerList = mutableListOf<String>()
    while (textShift <= textLength - 1) {
        println("\nShift: $textShift")
        var shifted = false

        if (textShift + shortestPatternLength > textLength) {
            println("The shortest pattern \"$patterns[0]\" is out of bounds, all ")
            break
        }

        for (pattern in sortedPatterns) {
            var innerShift = shortestPatternLength
            println("checking if text[${innerShift}] (${text[innerShift]}) == pattern[${innerShift}] (${pattern[innerShift]})")

            if (patternSkipMarkerList.contains(pattern)) {
                continue
            }

            if (pattern == sortedPatterns[0]) {
                while (
                    innerShift >= 0 &&
                    text[textShift + innerShift] == pattern[innerShift]
                ) {
                    println("Match at text[textShift + innerShift] (${text[textShift + innerShift]}) == pattern[innerShift] (${pattern[innerShift]}) ")
                    innerShift--
                }
            } else if (text[textShift + innerShift] == pattern[textShift + innerShift]) {
                accumulation += shortestPatternLength
            } else {
                patternSkipMarkerList.add(pattern)      // kalok ternyata nggak sama dan bukan index 1, gugurkan
            }

            if (pattern == sortedPatterns[0] && textShift < 0) {
                println("Pattern \"$pattern\" found at index $textShift")
                occurrences[pattern]!!.add(textShift)
                textShift++ // Geser satu karakter untuk mencegah overlap
                shifted = true
                break // Keluar dari loop pola setelah menemukan satu pola
            }

            else if (accumulation == pattern.length) {
                var accumulationCopy = accumulation
                print("check another unchecked word with while loop dengan jarak shortest pattern length")

                while (
                    accumulationCopy >= 0 &&
                    text[textShift + accumulationCopy] == pattern[accumulationCopy]
                ) {
                    println("Match at text[textShift + innerShift] (${text[textShift + accumulationCopy]}) == pattern[innerShift] (${pattern[accumulationCopy]}) ")
                    accumulationCopy--

                    if (textShift + accumulationCopy % shortestPatternLength == 0) {
                        accumulationCopy--
                    }
                }

                if (accumulationCopy < 0) {
                    println("Pattern \"$pattern\" found at index $textShift")
                    occurrences[pattern]!!.add(textShift)
                    textShift++
                    shifted = true
                    break
                }
            }

            else {
                print("missmatch")
            }
        }

        if (!shifted) { // Jika tidak ada pola yang ditemukan pada shift saat ini
            var shiftAmount = 1
            if (textShift < textLength) {
                val badCharPos = newBadCharTable[text[textShift]]
                if (badCharPos != null) {
                    shiftAmount = max(1, 0 - badCharPos)
                }
                println("Bad character '${text[textShift]}' at index $textShift, shift by $shiftAmount")
            }
            textShift += shiftAmount
        }
//        sortedPatterns.forEachIndexed { index: Int, pattern: String ->
//            var innerShift = shortestPatternLength
//            println("checking if text[${innerShift}] (${text[innerShift]}) == pattern[${innerShift}] (${pattern[innerShift]})")
//
//            if (patternSkipMarkerList.contains(pattern)) {
//                continue
//            }
//
//            if (index == 0) {
//                while (
//                    innerShift >= 0 &&
//                    text[textShift + innerShift] == pattern[innerShift]
//                ) {
//                    println("Match at text[textShift + innerShift] (${text[textShift + innerShift]}) == pattern[innerShift] (${pattern[innerShift]}) ")
//                    innerShift--
//                }
//            } else if (text[textShift + innerShift] == pattern[textShift + innerShift]) {
//                accumulation = shortestPatternLength
//            } else {
//                patternSkipMarkerList.add(pattern)      // kalok ternyata nggak sama dan bukan index 1, gugurkan
//            }
//
//            if (index == 0 && innerShift < 0) {
//            }
//
//            else if (accumulation == pattern.length) {
//                print("check another unchecked word with while loop dengan jarak shortest pattern length")
//            }
//
//            else {
//                print("missmatch")
//            }
//        }

//        textShift += shortestPatternLength  // todo : cuma pengisi biar nggak infinite loop => rubah nanti
    }

//    for (pattern in sortedPatterns) {
//        var innerShift = shortestPatternLength
//        println("checking if text[${innerShift}] (${text[innerShift]}) == patterns[pattern][${innerShift}] (${pattern[innerShift]})")
//
//        while (
//            innerShift >= 0 &&
//            text[shortestPatternLength + innerShift] == pattern[shortestPatternLength + innerShift]
//        ) {
//            innerShift--
//        }
//        if (innerShift < 0) {
//
//        }
//
//        else {
//            print("missmatch")
//        }
//    }

//    // this one is the one i looking for
//    for (pattern in sortedPatterns.indices) {
//        var innerShift = shortestPatternLength
//        println("checking if text[${shortestPatternLength}] (${text[shortestPatternLength]}) == patterns[pattern][${shortestPatternLength}] (${patterns[pattern][shortestPatternLength]})")
//
//        // if pattern index 0 (terpendek) cocok loop behind kek boyer moore biasa
//        while (
//            innerShift >= 0 &&
//            text[shortestPatternLength + innerShift] == patterns[pattern][shortestPatternLength + innerShift]
//        ) {
//            for (pattern in sortedPatterns) {
//                pattern[shortestPatternLength + innerShift]
//            }
//            println("checking if text[${shortestPatternLength}] (${text[shortestPatternLength]}) == patterns[pattern][${shortestPatternLength}] (${patterns[pattern][shortestPatternLength]})")
//            innerShift--
//        }
//
//        if (innerShift < 0) {
//            println("Pattern \"$pattern\" found at index $innerShift")
//            occurrences[pattern]!!.add(shift)
//            shift++ // Geser satu karakter untuk mencegah overlap
//            shifted = true
//            break // Keluar dari loop pola setelah menemukan satu pola
//        } else {
//            println("Mismatch at text[${shift + j}] != pattern[$j] ('${text[shift + j]}' vs '${pattern[j]}')")
//        }
//
//        // if pattern index 0 (terpendek) tidak cocok shift
//        // if pattern index lainnya cocok simpan dan lanjutkan mengecek
//            // if pattern index 0 sudah mengecek teks input sepanjang pattern index lainnya, lakukan pengecekan pada yang belum di cek
//            // if nggak ketemu shift sesuai bad character / good suffix ato length dari pattern index 0
//        // if index lainnya tidak cocok gugurkan
//    }

    println("\nOccurrences:")
    occurrences.forEach { (pattern, indices) ->
        println("Pattern \"$pattern\" found at indices ${indices.joinToString(", ")}")
    }

    return occurrences
}

fun main() {
    val text = "rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah"
    val patterns = listOf("rp", "rupiah")
    val occurrences = boyerMooreDiy(text, patterns)

    if (occurrences.isEmpty()) {
        println("Tidak ada pola yang ditemukan.")
    } else {
        for ((pattern, indices) in occurrences) {
            println("$pattern ditemukan di indeks: $indices")
        }
    }
}

//
//// Preprocessing: Membuat tabel "bad character"
//badChar = buatTabelBadChar(pola)
//
//// Preprocessing: Membuat tabel "good suffix" (lebih kompleks, diringkas di sini)
//// goodSuffix = buatTabelGoodSuffix(pola)
//
//i = 0 // Indeks teks
//while i <= n - m:
//j = m - 1 // Indeks pola (mulai dari kanan)
//while j >= 0 dan pola[j] == teks[i + j]:
//j = j - 1
//
//if j < 0:
//// Pola ditemukan!
//return i // Mengembalikan indeks awal kemunculan pola di teks
//// Untuk mencari semua kemunculan, lanjutkan dengan:
//// i = i + geseranGoodSuffix(0)
//
//else:
//// Karakter tidak cocok
//geseranBadChar = max(1, j - badChar[teks[i + j]])
//// geseranGoodSuffix = geseranGoodSuffix(j) // Jika menggunakan tabel good suffix
//i = i + geseranBadChar // Memindahkan pola sesuai tabel bad character
//
//// Pola tidak ditemukan
//return -1
//
//// Fungsi untuk membuat tabel bad character
//function buatTabelBadChar(pola):
//m = panjang(pola)
//tabel = tabel dengan ukuran alphabet (misal 256 untuk ASCII)
//inisialisasi semua nilai tabel dengan -1
//for i = 0 sampai m - 1:
//tabel[pola[i]] = i
//return tabel
