package com.example.speechnancial.testing

import com.example.speechnancial.tools.algoritma.boyerMooreMultiplePatternsWithReturn
import com.example.speechnancial.tools.algoritma.working.inputtedTextToTransactionsConverter
import java.io.File
import kotlin.system.measureNanoTime

fun main() {
    // Uji untuk berbagai panjang kata dari belakang
    val panjangUji = listOf(10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000)

    for (jumlahKata in panjangUji) {
        myBoyerMooreTesting(jumlahKata)
    }
}

fun ambilKataDariBelakang(teks: String, jumlahKata: Int): String {
    val daftarKata = teks.trim().split("\\s+".toRegex())
    return daftarKata.takeLast(jumlahKata).joinToString(" ")
}

fun myBoyerMooreTesting(jumlahKata: Int) {
    // Baca file
    val file = File("app/src/main/java/com/example/speechnancial/testing/kalimat_100000_kata_spok_akhir_rupiah.txt")
    if (!file.exists()) {
        println("❌ File tidak ditemukan!")
        return
    }

    val isiTeks = file.readText()
    val kataUji = ambilKataDariBelakang(isiTeks, jumlahKata)

    val runtimeRp = measureNanoTime {
        inputtedTextToTransactionsConverter(
            text = kataUji,
            occurrences = boyerMooreMultiplePatternsWithReturn(kataUji)
        )
    }

    println("✅ Runtime untuk $jumlahKata kata: $runtimeRp ns")
}

