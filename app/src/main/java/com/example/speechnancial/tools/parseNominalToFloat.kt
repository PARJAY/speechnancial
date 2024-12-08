package com.example.speechnancial.tools

import android.util.Log

fun main() {
    val nominals = sequenceOf(
        "rp100 miliar 100 juta 100.000,001",
        "rp 25.000",
        "rp 250.000",
        "rp 2.500.000",
        "rp 25.000.000",
        "rp 250.000.000",
        "rp 2.500.000.000",
        "rp 25.000.000.000",
        "rp 250.000.000.000",
        "rp 2.500.000.000.000",
        "rp 25.000.000.000.000",
        "rp 25.000.000.000.000",
        "rp. 45.000.000.000.000",
        "rp 12 miliar 11 juta 19.111",
        "12 miliar 11 juta 19.111 rupiah"
    )

    val newNominals = nominals.map { removeRp(it) }

    println(newNominals.toList())

    newNominals.forEach {
        println(parseNominalToFloat(it))
        println(parseNominalToFloat(it).toString())
        println()
    }
}

fun removeRp(nominal: String): String {
    val trimmedNominal = nominal.trim()
    return trimmedNominal.replace(Regex("(\\s*rp\\.?\\s*|\\s*rupiah\\s*)"), "")
}

fun parseNominalToFloat(nominalString: String): Float {
    val cleanedString = removeRp(
        nominalString.replace(".", "").replace(",", ".")
    )
    
    val nominalParts = cleanedString.split(" ")

//    Log.d("cleanedString", cleanedString)
//    nominalParts.forEach {
//        Log.d("nominalParts", it)
//    }

    var result = 0.0f
    var tempNumber = 0.0f

    val multipliers = mapOf(
        "puluh" to 10f,
        "ratus" to 100f,
        "ribu" to 1_000f,
        "juta" to 1_000_000f,
        "miliar" to 1_000_000_000f,
        "triliun" to 1_000_000_000_000f
    )

    for (part in nominalParts) {
        if (part.toDoubleOrNull() != null) tempNumber = part.toFloat()
        else {
            result += tempNumber * multipliers.getOrElse(part) { 1f }
            tempNumber = 0.0f
        }
    }

//    Log.d("parseNominalToFloat", "" + result + tempNumber)

    return result + tempNumber
}