package com.example.speechnancial.testing

import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import java.io.File
import java.io.FileWriter
import kotlin.system.measureNanoTime

fun main() {
    bMWordTestingRemake()
}

fun bMWordTestingRemake() {
    val testCase = listOf(
        "pengeluaran risol 5.000 rupiah",
        "pengeluaran mie ayam bakso 10.000 rupiah",
        "pengeluaran ayam geprek pedas level 5 12000 rupiah",
        "pengeluaran le minerale kecil untuk minum siang 6.000 rupiah",
        "pengeluaran beli cimory tiramisu di kantin kampus jimbaran 8000 rupiah",
        "pengeluaran beli dua indomilk uht 950 ml di indomaret harga diskon 33.800 rupiah",
        "pengeluaran beli bakso aci dengan telur di pedagang pinggir jalan depan rumah 20000 rupiah",
        "pengeluaran beli mie instan, telur satu sak, sayur bayam, kol, sawi, pokcoy, dan wortel 14000 rupiah",
        "pengeluaran belanja cimory pizza, pangsit, mi kuah, air mineral di kantin unud jimbaran siang ini 17000 rupiah",
        "pengeluaran membeli lauk ayam, sayur, telor satu sak, rice bowl, dan buah pisang di pasar sengol batubulan 150000 rupiah"
    )

    val charCounts = testCase.associateWith { it.length }

    val csvFile = File("bm_runtime_test.csv") // Kamu bisa ganti path-nya
    val writer = FileWriter(csvFile)

    // Header CSV
    val header = listOf("Iterasi") + testCase.map { charCounts[it].toString() }
    writer.appendLine(header.joinToString(","))

    // Lakukan 1 juta iterasi
    for (i in 1..1_000_000) {
        val currentIterationRuntimes = testCase.map { kataUji ->
            measureNanoTime {
                boyerMooreMultiplePatternsWithReturn(kataUji)
            }
        }

        val row = listOf(i.toString()) + currentIterationRuntimes.map { it.toString() }
        writer.appendLine(row.joinToString(","))
    }

    writer.flush()
    writer.close()

    println("Hasil testing telah disimpan ke: ${csvFile.absolutePath}")
}
