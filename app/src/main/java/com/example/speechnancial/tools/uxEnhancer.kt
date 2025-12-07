package com.example.speechnancial.tools

import com.example.speechnancial.tools.Util.Companion.parseToFloatSystem

fun main() {
    val inputs = listOf("1000", "100000", "100000", "1000000", "10000000", "100000000", "1000000000", "10000000000")

    for (input in inputs) {
        val tampil = getDisplayText(input)
        val masuk = parseToFloatSystem(tampil)
        println("tampil: $tampil")
        println("masuk: $masuk\n")
    }
}

fun getDisplayText(inputedNumber: String): String {
    return formatToThousandsSeparator(inputedNumber.toFloat())
}