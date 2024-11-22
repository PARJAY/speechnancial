package com.example.speechnancial.tools

import java.time.LocalDateTime


fun String.floatOrString() = toFloatOrNull() ?: this

fun getDateForTransaction() = LocalDateTime.now().toString()