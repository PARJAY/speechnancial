package com.example.speechnancial.common

import com.example.speechnancial.tools.boyerMooreHorspoolSearch


// Transaction Related Start
val nominalUnits = mapOf(
    "puluh" to 10.0,
    "ratus" to 100.0,
    "ribu" to 1_000.0,
    "juta" to 1_000_000.0,
    "miliar" to 1_000_000_000.0,
    "triliun" to 1_000_000_000_000.0
)

val pengeluaranKeywords =
    setOf(
        "beli",
        "bayar",
        "belanja",
        "transportasi",
        "tagihan",
        "sewa",
        "listrik",
        "internet",
        "air",
        "pendidikan",
        "makan",
        "minum",
        "perawatan",
        "kesehatan",
        "hiburan",
        "cicilan",
        "donasi",
        "asuransi",
        "pajak",
        "hobi",
        "liburan",
        "biaya administrasi"
    )
val pemasukanKeywords =
    setOf(
        "gaji",
        "bonus",
        "hadiah",
        "laba",
        "pengembalian",
        "investasi",
        "bunga",
        "deposito",
        "penjualan",
        "komisi",
        "royalti",
        "sumbangan",
        "uang kaget",
        "thr",
        "refund",
        "insentif",
        "hibah",
        "uang saku",
        "penghargaan",
        "dividen"
    )

// Transaction Related End