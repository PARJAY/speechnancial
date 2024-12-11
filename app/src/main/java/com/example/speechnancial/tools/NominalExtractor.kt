package com.example.speechnancial.tools

fun nominalExtractor(userRawInput : String) : Sequence<String> {
    val daftarDigitDalamKata = listOf("puluh","ratus","ribu","juta","miliar","triliun")
    val daftarDigitDalamKataRegex = daftarDigitDalamKata.joinToString("|")

    // rp [nominal]
    // 123 [digit kata] [nominal] rupiah

    /*
    // nominalNumber simplified :
    // any digit without . or ,                     ex : rp122222221121
    // 1-3 digit with "." and 1-3 looping           ex : rp.789.123
    // 1-3 digit with "," followed by any digit     ex : rp.789.123,456789012312389123
    * */

    // user ngomong -> specech recognition -> regex -> transaksi sah
    val rule =
        (
            "(" +
                "(" +
                    "\\s(${daftarDigitDalamKataRegex})" +
                    "(\\s\\d{1,3})?" +
                ")+" +
                "|(\\.?\\d{1,3})*(,\\d+)?" +
                "|\\d+" +
            ")*"
        ).toRegex()

    val regex =
        (
            "rp.?\\s?\\d{1,3}" + rule +
            "|" + rule + "\\srupiah"
        ).toRegex()
    val matches = regex.findAll(userRawInput)

    // nominal digit kata to float
    return matches.map { it.value }
}
