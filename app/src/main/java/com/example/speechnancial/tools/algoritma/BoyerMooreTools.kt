package com.example.speechnancial.tools.algoritma

// TODO : not tested yet
fun lastToFirstChecker(
    text : String,
    shortestPatternLength : Int,
    pattern : String,
    isIndexZero : Boolean = true
) : Int {
    var textShift = 0

    var patternShift =
        if (isIndexZero) shortestPatternLength - 1
        else pattern.length - 1

//    println("text[textShift] (${text[textShift]}) == pattern[textShift] (${pattern[textShift]}) => ${text[textShift] == pattern[textShift]}")
    while (
        patternShift >= 0 &&
        text[textShift + patternShift] == pattern[patternShift]
    ) {
        println("text[textShift] (${text[textShift]}) == pattern[textShift] (${pattern[textShift]}) => ${text[textShift] == pattern[textShift]} | textShift = $textShift")
        patternShift--

        if (!isIndexZero && patternShift % shortestPatternLength == 0) {
            println("!isIndexZero ${false} && textShift {${textShift}} % shortestPatternLength {${shortestPatternLength}} == 0 {${textShift % shortestPatternLength == 0}}")
            patternShift--
        }
    }

//    if (textShift < 0) {
//        println("Pattern \"$pattern\" found at index $shift")
//        occurrences[pattern]!!.add(shift)
//        shift++ // Geser satu karakter untuk mencegah overlap
//        shifted = true
//        break // Keluar dari loop pola setelah menemukan satu pola
//    } else {
//        println("Mismatch at text[${shift + j}] != pattern[$j] ('${text[shift + j]}' vs '${pattern[j]}')")
//    }

    return textShift
}

fun main() {
    // test them here

    val shortestPatternLength = "rp".length

    println( "rp : " +
        lastToFirstChecker(
            text = "rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah",
            shortestPatternLength,
            pattern = "rp",
            isIndexZero = true
        )
    )

    println( "rupiah : " +
        lastToFirstChecker(
            text = "rpiah teks rupiah untuk mencari rp dan rupiah lagi rupiah",
            shortestPatternLength,
            pattern = "rupiah",
            isIndexZero = false
        )
    )

    // pattern ("rp" "rupiah")
}