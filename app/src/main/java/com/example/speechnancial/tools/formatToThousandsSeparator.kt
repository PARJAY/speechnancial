package com.example.speechnancial.tools

import java.text.DecimalFormat

fun formatToThousandsSeparator(value: Float): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(value).replace(",", ".") // Replace ',' with '.'
}