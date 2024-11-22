package com.example.speechnancial.tools

// simple case
// parkir rp.2000
// 2000 rupiah

// how to check kalo isinya angka doang?
fun findNominal(splittedSource: List<String>): String {
    val penandaRupiah = "rupiah"
    val penandaRp = "rp."

    val lastWord = splittedSource.last()
    lastWord.floatOrString()

    // case : 2000 rupiah => take the 2000
    if (boyerMooreHorspoolSearch(lastWord, penandaRupiah) != -1)
        return splittedSource.first()

    // case : rp.2000 => take the 2000
    if (boyerMooreHorspoolSearch(lastWord, penandaRp) != -1)
        return lastWord.split(".").last()

    return "nominal tidak ditemukan"
}


