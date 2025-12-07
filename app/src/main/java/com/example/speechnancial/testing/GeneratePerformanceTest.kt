package com.example.speechnancial.testing

import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import com.example.speechnancial.tools.algoritma.working.inputtedTextToTransactionsConverter
import kotlin.system.measureNanoTime
import java.io.File
import java.io.FileWriter

fun main() {
    val transactionType = "pengeluaran"
    val worstCaseWord = "rupiah"
    val nominalMarkerRp = "rp"
    val nominalMarkerRupiah = "rupiah"
    val firstNominal = "1"
    val continueNominal = "0"

    // Nama file output tunggal
    val outputFile = File("benchmark_results.csv")
    // Pastikan direktori ada dan file baru dibuat/ditimpa
    outputFile.parentFile?.mkdirs()
    outputFile.writeText("Length,Runtime_nano,PatternType\n")

    // Kita gunakan FileWriter dalam mode append = true
    val writer = FileWriter(outputFile, true)

    for (i in 0..1_000_000_000) {
        // Buat pola sesuai iterasi
        val repeatedRupiah = worstCaseWord.repeat(i)
        val nominalValue = firstNominal + continueNominal.repeat(i)

        val patternRp = transactionType + repeatedRupiah + nominalMarkerRp + nominalValue
        val patternRupiah = transactionType + repeatedRupiah + nominalValue + nominalMarkerRupiah

        // Ukur runtime untuk pattern “…rp…”
        val runtimeRp = measureNanoTime {
            inputtedTextToTransactionsConverter(
                patternRp,
                boyerMooreMultiplePatternsWithReturn(patternRp)
            )
        }
        // Tulis langsung ke CSV
        writer.append("rp,${patternRp.length},$runtimeRp\n")

        // Ukur runtime untuk pattern “…rupiah…”
        val runtimeRupiah = measureNanoTime {
            inputtedTextToTransactionsConverter(
                patternRupiah,
                boyerMooreMultiplePatternsWithReturn(patternRupiah)
            )
        }
        // Tulis langsung ke CSV
        writer.append("rupiah,${patternRupiah.length},$runtimeRupiah\n")

//        if (patternRp.length > 5000) break // opsional untuk membatasi panjang
    }

    writer.flush()
    writer.close()
    println("Hasil telah disimpan ke benchmark_results.csv")
}