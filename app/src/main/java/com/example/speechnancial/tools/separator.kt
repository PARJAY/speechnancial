package com.example.speechnancial.tools

import androidx.compose.runtime.MutableState
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction

// todo : now i dont even understand what im coding back then
// todo : i want to update this
//  functiontipe transaksi :
//  - pisahkan kata pertama
//  - cari apakah ada keyword transaction type
//  ----------------------------------------------
//  nominal => mulai deteksi dari kata kedua
//  -> cari apakah ada kata kunci rp
//    -> jika rp cek kata dibelakangnya apakah ada juta atau 1-9
//  -> cari apakah ada kata kunci rupiah
//	  -> jika ada cek kata didepannya apakah ada juta atau 1-9
fun separator(
    splittedSource: List<String>,
    transactionResult : MutableState<Transaction>
) {
    transactionResult.value = transactionResult.value.copy(
        type = findTransactionType(splittedSource[0])
    )

    if (transactionResult.value.type == TransactionType.UNDEFINED)
        transactionResult.value = transactionResult.value.copy(description = splittedSource[0])


    // 2000 rupiah -> 2000
    // rp2.000 -> 2000
    // rp.2.000 -> 2000
    // rp 2000 -> 2000
    // rp. 2000 -> 2000

    // kenapa user nggak bisa setting setting aja nih
    // input nominal template
        // rp[nominal]
        // rp.[nominal]
        // rp [nominal]
        // rp. [nominal]
        // [nominal] rupiah
    val penandaRupiah = "rupiah"
    val penandaRp = "rp"
    val penandaRp2 = "rp."

    // Step 2: Loop pada kata-kata berikutnya
    var i = 1
    while (i < splittedSource.size) {
        val currentWord = splittedSource[i]

        val isNumeric = currentWord.toFloatOrNull() != null

        if (isNumeric) {
            if (
                i + 1 < splittedSource.size &&
                boyerMooreHorspoolSearch(splittedSource[i + 1], penandaRupiah) != -1
            ) {
                transactionResult.value = transactionResult.value.copy(
                    nominal = (currentWord + " " + splittedSource[i + 1]).toFloat()
                )
                i++
            } else
                transactionResult.value = transactionResult.value.copy(
                    description = transactionResult.value.description + " $currentWord"
                )
        }
        else if (boyerMooreHorspoolSearch(currentWord, penandaRp) != -1)
            transactionResult.value = transactionResult.value.copy(
                nominal = (currentWord.split("rp").joinToString(separator = "").split(".").joinToString("")).toFloat()
            )
        else
            transactionResult.value = transactionResult.value.copy(
                description = transactionResult.value.description + " $currentWord"
            )
        i++
    }
}


// older function

//fun separator(
//    splittedSource: List<String>,
//    onResult: (type: String, description: String, nominal: String) -> Unit
//) {
//    val result = arrayListOf("", "", "")
//    Log.d("p", "masuk")
//    result[0] = findTransactionType(splittedSource[0])
//
//    if (result[0]  == "Tidak Diketahui")
//        result[1] = splittedSource[0]
//
//    Log.d("p 2 ", result[0] )
//    val penandaRupiah = "rupiah"
//    val penandaRp = "rp"
//
//    // Step 2: Loop pada kata-kata berikutnya
//    var i = 1
//    while (i < splittedSource.size) {
//        val currentWord = splittedSource[i]
//
//        val isNumeric = currentWord.toFloatOrNull() != null
//
//        if (isNumeric) {
//            if (
//                i + 1 < splittedSource.size &&
//                boyerMooreHorspoolSearch(splittedSource[i + 1], penandaRupiah) != -1
//            ) {
//                result[2]  = currentWord + " " + splittedSource[i + 1]
//                i++
//            } else result[1] += " $currentWord"
//        }
//        else if (boyerMooreHorspoolSearch(currentWord, penandaRp) != -1)
//            result[2]  = currentWord.split("rp").joinToString(separator = "").split(".").joinToString("")
//        else result[1] += " $currentWord"
//        i++
//    }
//
//    onResult(result[0], result[1].trim(), result[2])
//}

// how to check kalo isinya angka doang?
//fun separator(splittedSource: List<String>): List<String> {
//    val result = arrayListOf("", "", "")
//    result[0] = findTransactionType(splittedSource[0])
//
//    val penandaRupiah = "rupiah"
//    val penandaRp = "rp."
//
//    // for each remaining splitted sources
//    // check if can be converted to float                       (case : 2000 rupiah)
//        // check if the word after this is a nominal related
//            // if not, capture all to result[1] / description
//            // if yes, capture all to result[2] / nominal
//    // check if  its a nominal related                          (case : rp.2000)
//        // if not, capture to result[1] / description
//        // if yes, capture to result[2] / nominal
//
//    splittedSource.forEachI { it ->
//        // return just nominal
//        if (boyerMooreHorspoolSearch(it, penandaRupiah) != -1)
//            return
//
//        // return just nominal
//        if (boyerMooreHorspoolSearch(it, penandaRp) != -1)
//            return it.split(".").last()
////        if (it.floatOrString())
//    }
//
//    return "nominal tidak ditemukan"
//}

// chatGPT generated

// new function
//fun separator(
//    splittedSource: List<String>,
//    transactionResult : MutableState<TransactionResult>,
//    pembeda : String
//) {
//    val transactionType = findTransactionType(splittedSource[0])
//
//    if (transactionType != "Tidak Diketahui") transactionResult.value.type = splittedSource[0] + " : " + transactionType
//    else {
//        transactionResult.value.type = "-1"
//        transactionResult.value.description = splittedSource[0]
//    }
//
//    val result = arrayListOf("", "", "") // [Jenis Transaksi, Deskripsi, Nominal]
//    val penandaRupiah = "rupiah"
//    val penandaRp = "rp."
//
//    // Cek tipe transaksi atau masukkan ke deskripsi jika tidak diketahui
//    transactionResult.value.type = findTransactionType(splittedSource[0]).takeIf { it != "Tidak Diketahui" } ?: ""
//    transactionResult.value.description = splittedSource[0].takeIf { result[0].isEmpty() } ?: ""
//
//    // Loop untuk mencari nominal atau deskripsi
//    splittedSource.drop(1).forEachIndexed { i, currentWord ->
//        val isNumeric = currentWord.toFloatOrNull() != null
//
//        when {
//            isNumeric && i + 1 < splittedSource.size &&
//                    boyerMooreHorspoolSearch(splittedSource[i + 1], penandaRupiah) != -1
//            -> transactionResult.value.nominal = "$currentWord ${splittedSource[i + 1]}"
//
//            isNumeric -> transactionResult.value.description += " $currentWord"
//            boyerMooreHorspoolSearch(currentWord, penandaRp) != -1 ->
//                transactionResult.value.nominal = currentWord.split(".").joinToString(" ")
//            else -> transactionResult.value.description += " $currentWord"
//        }
//    }
//}
//
//fun separator(
//    splittedSource: List<String>,
//    transactionResult : MutableState<TransactionResult>,
//    pembeda3: Int
//) {
//    transactionResult.value.type = findTransactionType(splittedSource[0])
//    if (transactionResult.value.type == "Tidak Diketahui")
//        transactionResult.value.description = splittedSource[0]
//
//    val penandaRupiah = "rupiah"
//    val penandaRp = "rp."
//
//    // since the 1 is dropped, have to use 2 when finding next index
//    splittedSource.drop(1).forEachIndexed { i, word ->
//        val isNumeric = word.toFloatOrNull() != null
//        if (isNumeric) {
//            Log.d("isN : ", "passed")
//            Log.d("current word : ", word)
//            Log.d("splittedSource[i + 2] : ", splittedSource[i + 2])
//
//            Log.d("rupiah checker : ",
//                boyerMooreHorspoolSearch(splittedSource[i + 2], penandaRupiah).toString()
//            )
//            if (
//                i + 2 < splittedSource.size &&
//                boyerMooreHorspoolSearch(splittedSource[i + 2], penandaRupiah) != -1
//            ) {
//                Log.d("isN - if [before] : ", transactionResult.value.nominal)
//                transactionResult.value.nominal = "$word ${splittedSource[i + 2]}"
//                Log.d("isN - if [after] : ", transactionResult.value.nominal)
//            }
//            else {
//                Log.d("isN - else [before] : ", transactionResult.value.description)
//                transactionResult.value.description += " $word"
//                Log.d("isN - else [after] : ", transactionResult.value.description)
//            }
//        }
//        else if (boyerMooreHorspoolSearch(word, penandaRp) != -1) {
//            Log.d("else if [before] : ", transactionResult.value.nominal)
//            transactionResult.value.nominal = word.split(".").joinToString(separator = " ")
//            Log.d("else if [after] : ", transactionResult.value.nominal)
//        }
//
//        else {
//            Log.d("else [before] : ", transactionResult.value.description)
//            transactionResult.value.description += " $word"
//            Log.d("else [after] : ", transactionResult.value.description)
//        }
//    }
//}