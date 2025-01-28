package com.example.speechnancial.tools

import com.example.speechnancial.tools.algoritma.working.boyerMooreSearchMultiplePatterns
import com.example.speechnancial.tools.algoritma.working.nominalDescriptionSeparator

fun main() {
    val testCaseData = listOf(
        "Gek sita ngebalikin uangku gara-gara beli polybag rp10.000",
        "Pengeluaran nasi campur 10.000 rupiah",
        "Beli termos 500 ml rp50.000",
        "Beli ramen instan rp15.000",
        "Ayam geprek 12.000 rupiah",
        "Telur 2 pak rp104.000",
        "Ardi meminjam uang rp20.000",
        "Pertamax 40.000 rupiah",
        "Baterai 13000 rupiah",
        "Korek rp3.000",
        "Sempol rp5.000",
        "Burger 15.000",
        "Joel minta uang rp2.000",
        "Baterai 8 biji rp20.000",
        "Chitato dan nasi bungkus rp12.000",
        "Aqua botol rp5.000",
        "Joel minta uang rp5.000",
        "Ayam goreng rp10.000",
        "Nasi bungkus pantai germas rp10.000",
        "Lapar mata Cimory 7.000 rupiah",
        "Lauk campur rp10.000",
        "Geprek 16.000 rupiah",
        "Ack rp32.500",
        "Dada ayam rp10.000",
        "Dada ayam rp10.000",
        "Martabak spesial rp22.000",
        "Nasi bungkus Rp10.000",
        "Jajan rp8.000",
        "Burger Pantai Gili mas rp15.000",
        "Susu Bear Brand sekarang 11.000 doang anjir",
        "Nasi bungkus rp10.000",
        "Ivan minjem uang rp100.000",
        "Ivan kembaliin uang rp70.000",
        "Ivan minjem lagi rp30.000",
        "Ivan ngembaliin rp50.000 jadi hutangnya udah lunas",
        "Jempol rp5.000",
        "Nasi kuning bungkus sama roti Indomaret rp20.000 total",
        "Belanja 2 slim fit and cookies rp9.000",
        "Diutangin Indra lalapan 15.000 dan utang Jordan parkir rp2.000",
        "Richeese nabati raspbery yoghurt rp2.000",
        "Nasi kuning campur Rp10.000",
        "Parkir rp2.000",
        "Kebab dan burger crispy plus biasa rp25.000",
        "Pertamax rp40.000",
        "Dark wonder coklat dapat diskon jadi rp9.500",
        "Nasi campur Rp10.000",
        "Bekal rp100.000 pemasukan",
        "Lalapan 28.000 rupiah",
        "Belanja oatmeal dan rexona di Indomaret dengan harga murah rp123.400",
        "Bayarin rama gojek 17 ribu rupiah",
        "Beli spaghetti rp38.000",
        "Beli power bank 20000mah (74wh) xiaomi - rp300.000",
        "Beli kabel HDMI ke HDMI 3 m 58.500 rupiah",
        "Bayar parkir rp2.000",
        "Bayar parkir lagi rp2.000",
        "Bayar parkir kemarin rp.2000",
        "Beli monitor 27 inch make qris rp.1662960",
        "Di transfee ibuk rp2000000 kemarin",
        "Bayar parkir rp2.000",
        "Bayar parkir living world rp2.000",
        "Kfc burger ayam minum + gratis sop rp60000",
        "Beli telor 1 krat rp.50000",
        "Servis motor rp100.000",
        "Pertamax 40.000 rupiah",
        "Tempe sama saus tiram sachet rp8.000",
        "Steak ayam crispy plus kentang dua bungkus Rp40.000",
        "Masker kotak 50 pcs Rp20.000",
        "Dada fillet seperempat kilo Rp14.000",
        "Telur satu kerat 48.000 rupiah dada ayam potong seperempat kilo Rp11.000",
        "Jeruk nipis seperempat kilo Rp10.000",
        "8 September - Iwak kampret minta uang Rp20.000 buat bayar ayam banten pas di tusan kemarin",
        "Bayar parkir RSUD primagama Rp1.000",
        "Kemarin beli Cimory tiramisu di kampus Rp8.000",
        "Kemarin beli air mineral juga di kampus Rp5.000",
        "Beli dua Indomilk UHT 950 ML di indomaret dengan harga diskon 33.800 rupiah",
        "Dada ayam seperempat kilo rp15.000 dan tempe 1 kotak rp5.000",
        "Beli Cimory tiramisu di kampus 8.000 rupiah",
        "Beli 2 Indomaret UHT 950 mili 33.800 rupiah rupiah rupiah rupiah",
        "Belanja di kampus Cimory pizza sama pangsit 17.000 rupiah",
        "Belanja di Lotte grosir 174.850 rupiah",
        "Bayar parkir Lotte Mart grosir 2500 rupiah",
        "Beli Pertamax Rp50.000",
        "Tadi juga beli martabak Jepang Rp25.000",
        "Pagi beli Laklak sama bikang Rp2.000 habis itu ke pasar Oh sebelum ke pasar beli risol Rp3.000 habis itu di pasar nemu dagang tuna ikan mentah beli tuna 1/4 kg kurang 11.000 rupiah setelah itu ke Tohpati di sana dapat kelepon Rp5.000",
        "Tadi pagi kayaknya juga ke Bu Ita beli daun jeruk sama kol Rp5.000 habis itu ke Bu perempatan beli tahu Rp4.000",
        "Tuna 1/4 kg 14.000 rupiah",
        "Tuna 1/4 kg 12000 rupiah Tahu Tempe 10000 rupiah",
        "Sore ini belanja di Unud kampus Unud kantinnya belanja Cimory matcha sama dilan cookies choco chip rp10.000",
        "Ack unud nasi dada ayam pakai Milo 22.000 rupiah",
        "Beli Aqua 6000 rupiah",
        "Nambahin c burger rp4.000",
        "Sesari 5.000 rupiah",
        "Kuas beli kuas di toko bangunan rp5.000",
        "Pengumpat bawang rp15.000",
        "Beli out 1 kilo 40700 Rp",
        "Bayar 4 galon air rp52.000",
        "Beli Pertamax bensin rp40.000",
        "Beli abon ayam super 50 gram rp15.000",
        "ESP 32 rp100.000 breadboard 400 poin 28.000 dht 25.000 kabel jumper 45.000",
        "Esp32nya rp110",
        "THR buat Maman rp20.000",
        "Belanja collagen rp12.000",
        "Beli makan mie telor kantin 11.000 rupiah",
        "Beli bensin rp40.000",
        "Pemasukan 800.000 cash",
        "Bayar ojek 190.000 rupiah",
        "Beli mie goreng tukang kaki lima 15.000 rupiah",
        "Beli pulpen 5500 rupiah",
        "Bayar tol ke bandara 28.000 rupiah",
        "Makan di bandara rp65.000",
        "Catat pengeluaran gacoan 31.000 rupiah",
        "Bayar parkir gacoan rp2.000",
        "Pertamax rp40.000",
        "Daging tunas 1/4 kilo rp14.000 bikang rp2.000 telepon rp1.000",
        "Daging ayam seperempat kilo rp10.000",
        "Susu collagen rp12.000",
        "Santan 65 mili 4.000 rupiah",
        "Dada ayam seperempat kilo rp10.000",
        "Beli daun bawang juga tadi pagi rp2.000",
        "Malam ini beli canang rp12.000",
        "Beli daging ayam dada rp10.000 seperempat kilo",
        "Ayam goreng guguk dada rp20.000",
        "Telur satu krat 48.000 tuna 1/4 kg Rp14.000 pertama Rp40.000",
        "Beng-beng Kantin Sekolah kampus Unud 2 buah rp5.000",
        "Ayam goreng saus double Rp30.000",
        "Obat sariawan rp38000",
        "Belanja sayur sup 2000",
        "Belanja telur satu kerat 48.000 tuna 1/4 kilo 1513.000 dan tahu tempe Rp5.000",
        "Pertalite rp30.000",
    )

    // todo : hitung berapa datanya, tes pake regex mu, buat confusion matrix dan TENTUIN True False Negative Positivenya sendiri
    //  kata ku sih 100% akurat 😎👊

    testCaseData.forEach {
        val patterns = listOf("rp", "rupiah")

        println("Text: \"$it\"")
        println("Text Length: \"${it.length}\"")
        println("Patterns: $patterns\n")

        val occurrences = boyerMooreSearchMultiplePatterns(it, patterns)

        println("occurrences : ${occurrences.keys.joinToString(", ")}")
        println("nominal : ")
        nominalDescriptionSeparator(it, occurrences)

        println("_____________________________________________")
        println("")
    }

//    regex way
//    testCaseData.forEachIndexed { index, it ->
//        val generatedTransaction = createTransactionFromInput(it)
//        println("test case index     : $index")
//        println("inputted text       : $it")
//        println("rawText             : ${generatedTransaction.rawText}")
//        println("type                : ${generatedTransaction.type}")
//        println("details             : ${generatedTransaction.details}")
//        println("total               : ${generatedTransaction.total}")
//        println("isValid             : ${generatedTransaction.isValid}")
//        println("T/F (System)        : T")
//        println("P/N (Reality)       : P")
//        println("___________________________________________________")
//    }
//    println()
//    println()
//    println("testCaseData.size : ${testCaseData.size}")
}
