package com.example.speechnancial.tools

import com.example.speechnancial.common.TransactionType

fun findTransactionType(word: String): TransactionType {
    val daftarKataKunciPengeluaran = arrayListOf("belanja", "beli", "bayar")
    val daftarKataKunciPemasukan = arrayListOf("gajian", "bunga", "uang kaget")

    for (kataKunciPengeluaran in daftarKataKunciPengeluaran)
        if (boyerMooreHorspoolSearch(word, kataKunciPengeluaran) != -1)
            return TransactionType.OUTCOME

    for (kataKunciPemasukan in daftarKataKunciPemasukan)
        if (boyerMooreHorspoolSearch(word, kataKunciPemasukan) != -1)
            return TransactionType.INCOME

    return TransactionType.UNDEFINED
}

// old function
//fun findTransactionType(word: String): String {
//    val daftarKataKunciPengeluaran = arrayListOf("belanja", "beli", "bayar")
//    val daftarKataKunciPemasukan = arrayListOf("gajian", "bunga", "uang kaget")
//
//    for (kataKunciPengeluaran in daftarKataKunciPengeluaran)
//        if (boyerMooreHorspoolSearch(word, kataKunciPengeluaran) != -1)
//            return "$word -> Pengeluaran"
//
//    for (kataKunciPemasukan in daftarKataKunciPemasukan)
//        if (boyerMooreHorspoolSearch(word, kataKunciPemasukan) != -1)
//            return "$word -> Pemasukan"
//
//    return "Tidak Diketahui"
//}