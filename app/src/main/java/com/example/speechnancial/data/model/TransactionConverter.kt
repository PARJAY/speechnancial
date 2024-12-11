package com.example.speechnancial.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TransactionConverter {
    @TypeConverter
    fun fromList(details: List<TransactionDetail>): String {
        return Gson().toJson(details)
    }

    @TypeConverter
    fun toList(json: String): List<TransactionDetail> {
        val type = object : TypeToken<List<TransactionDetail>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}