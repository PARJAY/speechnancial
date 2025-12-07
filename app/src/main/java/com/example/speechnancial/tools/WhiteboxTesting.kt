package com.example.speechnancial.tools

import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturnAndDebug
import com.example.speechnancial.tools.algoritma.working.inputtedTextToTransactionsConverterWithDebug

fun runTestCase(input: String) {
    println("Input: $input")
    val converted = inputtedTextToTransactionsConverterWithDebug(
        input,
        boyerMooreMultiplePatternsWithReturnAndDebug(input)
    )
    println("Output: $converted")
    println("------")
}

fun main() {
    val testCase =
        "pengeluaran parkir rp2000 " +
        "pemasukan hadiah ulang tahun rupiah 50000 " +
        "transfer belanja rp3000 " +
        "setor tabungan rupiah 1.000 " +
        "tarik tunai rp1000 " +
        "hutang teman rupiah 20000 " +
        "piutang saudara rp500 " +
        "teksrandom tanpa angka rp " +
        "ini teks tanpa pola " +
        "rp2000 pengeluaran parkir " +
        "pemasukan hadiah rupiah seribu " +
        "pengeluaran rp2000 rp3000"

    runTestCase(testCase)
}