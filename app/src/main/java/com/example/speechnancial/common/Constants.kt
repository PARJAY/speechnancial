package com.example.speechnancial.common

import com.example.speechnancial.tools.boyerMooreHorspoolSearch

val nominalUnits = mapOf(
    "puluh" to 10.0,
    "ratus" to 100.0,
    "ribu" to 1_000.0,
    "juta" to 1_000_000.0,
    "miliar" to 1_000_000_000.0,
    "triliun" to 1_000_000_000_000.0
)

val nominalNumber = mapOf(
    "satu" to 1,
    "dua" to 2,
    "tiga" to 3,
    "empat" to 4,
    "lima" to 5,
    "enam" to 6,
    "tujuh" to 7,
    "delapan" to 8,
    "sembilan" to 9,
)

val imbuhanSpesial = "se"
val nominalSpesial = "belas"

/*

// studi kasus : keuntungan perusahaan satu miliar lima ratus ribu rupiah

move this function to other place
// se + [nominalUnits]
// "sebelas"
// "seratus"
// "seribu"
// "sejuta"

// misal ada kata nih.
//  komisi freelance programming lima juta rupiah
//  komisi profit perusahaan satu miliar lima ratus ribu rupiah
//  komisi freelance programming lima juta rupiah


// step 1 - ketemu rupiah
    // rupiah ditemukan pada kata paling akhir
// step 2 - cari terus kata sebelum rupiah yang termasuk nominal units / nominal sampai habis
    // step 2.n1 - ribu -> [] 000
    // step 2.n2 - ratus -> [] 00_000
    // step 2.n3 - lima -> 500_000
    // step 2.n4 - miliar -> [] 000_000_000 + 500_000
    // step 2.n5 - satu -> 1000_000_000 + 500_000
    // step 2.n6 - perusahaan -> bukan termasuk nominal units / nominal (berhenti sampai disini)
// step 3 - gabungkan dan jadikan float 1000_500_000f
// step 4 - tampilkan

// fun textNominalConverter
//fun main(text : String) {
//    text.split(" ")
//}

// assume step 1 is done in other place
*/



fun main() {
    val rawText = "keuntungan perusahaan satu miliar lima ratus ribu seratus dua belas".split(" ")

    var result = 0.0
    val currentMultiplier = 1.0
    var isNominalUnitBefore = false
    val defaultDigit = 1.0

    println(1_000_000_000.0)

    // akumulasi kata nominal unit, sampai ketemu kata nominal angka
    // belas 10
    // dua 2
    // seratus = 100 => 112
    // ribu = 1000
    // ratus = 100
    // lima = 5 * (1000 * 100) = 500000 + 112 = 500112
    // miliar = 1000000000
    // satu = 1 * (1000000000) + 500112

    rawText.reversed().forEach { word ->
        println(word)
        for (nominalUnit in nominalUnits)
            if (boyerMooreHorspoolSearch(word, nominalUnit.key) != -1) {
                if (isNominalUnitBefore) result *= defaultDigit * nominalUnit.value
                else result = defaultDigit * nominalUnit.value
                isNominalUnitBefore = true
                println("nominalUnit ($word) == (${nominalUnit.key}) matched")
                println("result = $result")

                break
            }

        for (nominalWord in nominalNumber)
            if (boyerMooreHorspoolSearch(word, nominalWord.key) != -1) {
                if (isNominalUnitBefore) result *= nominalNumber[word]!!
                else result+= nominalNumber[word]!!
                isNominalUnitBefore = false
                println("nominal word ($word) == (${nominalWord.key}) matched")
                println("result = $result")

                break
            }

        for (nominalWord in nominalNumber)
            if (boyerMooreHorspoolSearch(word, "$imbuhanSpesial${nominalWord.key}") != -1) {
                result += nominalNumber[word]!!
                isNominalUnitBefore = false
                println("nominal word ($word) == (${nominalWord.key}) matched")
                println("result = $result")

                break
            }

        else if (word == nominalSpesial) {
            isNominalUnitBefore = false
            result += 10
            println("word is belas")
            println("result = $result")
            break
        }
        else break
    }

    // Tambahkan nilai yang tersisa
//    result += currentValue * currentMultiplier
    println("total : " + result.toInt())
}

// Daftar kata kunci tipe transaksi
val pengeluaranKeywords = setOf("bayar", "beli", "belanja")
val pemasukanKeywords = setOf("gaji", "penjualan", "bonus")

val nominalKeywords1 = "rp "
val nominalKeywords2 = "rp. "
val nominalKeywords3 = "rupiah"