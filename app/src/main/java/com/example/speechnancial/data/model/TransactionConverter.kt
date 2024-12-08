package com.example.speechnancial.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

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
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}