package com.example.speechnancial.tools

fun boyerMooreHorspoolSearch(
    source: String,
    pattern: String
) : Int {
    val pattChar = pattern.toCharArray()
    val patternLength: Int = pattChar.size
    if (patternLength == 0) return 0    // return 0 artinya panjang char pada kata nggak sesuai

    val src = source.toCharArray()
    val srcLength: Int = src.size

    val shift = IntArray(200) { patternLength }
    for (k in 0..(patternLength - 2))
        shift[pattChar[k].code] = patternLength - 1 - k

    var i = 0
    var j: Int
    while ((i + patternLength) <= srcLength) {
        j = patternLength - 1
        while (source[i + j] == pattChar[j]) {
            j -= 1
            if (j < 0) return i     // return i artinya ditemukan pada index char ke "i"
        }
        i += shift[src[i + patternLength - 1].code]
    }
    return -1   // return -1 artinya tidak ditemukan
}

// string.isBoyerMooreSearchAvailable() {
//
// }