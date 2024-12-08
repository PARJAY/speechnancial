package com.example.speechnancial.tools

import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.common.pemasukanKeywords
import com.example.speechnancial.common.pengeluaranKeywords


// todo : nggak ada keharusan lagi untuk make boyer moore
fun findTransactionType(word: String): TransactionType {
    for (kataKunciPengeluaran in pengeluaranKeywords)
        if (boyerMooreHorspoolSearch(word, kataKunciPengeluaran) != -1)
            return TransactionType.OUTCOME

    for (kataKunciPemasukan in pemasukanKeywords)
        if (boyerMooreHorspoolSearch(word, kataKunciPemasukan) != -1)
            return TransactionType.INCOME

    return TransactionType.UNDEFINED
}