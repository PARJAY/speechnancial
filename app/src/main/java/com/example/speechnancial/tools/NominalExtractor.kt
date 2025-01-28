package com.example.speechnancial.tools

fun main() {
    nominalExtractorRegex("Belanja telur satu kerat 48.000 tuna 1/4 kilo 1513.000 dan tahu tempe Rp5.000").forEach {
        println(it)
    }

    nominalExtractorRegex("Belanja telur satu kerat 48.000 tuna 1/4 kilo 1513.000 dan tahu tempe Rp 5.000").forEach {
        println(it)
    }
}

fun nominalExtractorRegex(userRawInput : String) : Sequence<String> {
    val daftarDigitDalamKata = listOf("puluh","ratus","ribu","juta","miliar","triliun")
    val daftarDigitDalamKataRegex = daftarDigitDalamKata.joinToString("|")

    // rp [nominal]
    // 123 [digit kata] [nominal] rupiah

    /*
    // regex simplified :
    // ide pertama adalah angka dan kata digit sampek selesai
        // contoh : 123 juta 23 ribu
    // ide kedua adalah angka dengan titik dan koma
        // contoh : 123.456.789.000,1234567890
    // ide ketiga adalah angka tok
        // contoh : 1234567890
    * */

    // user ngomong -> specech recognition -> regex -> transaksi sah
    val rule =
        (
            "(?:" +
                "(?:" +
                    "\\s(${daftarDigitDalamKataRegex})" +
                    "(\\s\\d{1,3})?" +
                ")+" +
                "|(?:\\.?\\d{1,3})*(,\\d+)?" +
                "|\\d+" +
            ")*"
        ).toRegex()

    val regex =
        (
            "(?:rp|Rp|RP).?\\s?\\d{1,3}" + rule +
            "|\\d{1,3}" + rule + "\\s(?:rupiah|Rupiah)"
        ).toRegex()
    val matches = regex.findAll(userRawInput)

    // nominal digit kata to float
    return matches.map { it.value }
}
