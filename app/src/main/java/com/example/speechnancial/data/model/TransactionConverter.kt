package com.example.speechnancial.data.model

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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
    fun fromTimestamp(value: String?): LocalDateTime? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromMap(details: Map<String, Float>?): String? {
        return details?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toMap(json: String?): Map<String, Float>? {
        return json?.let {
            val type = object : TypeToken<Map<String, Float>>() {}.type
            Gson().fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromTimestamp(value: Timestamp?): String? {
        return value?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                .withZone(ZoneId.of("UTC")) // Pastikan zona waktu UTC
            formatter.format(it.toDate().toInstant())
        }
    }

    @TypeConverter
    fun toTimestamp(value: String?): Timestamp? {
        return value?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                .withZone(ZoneId.of("UTC")) // Pastikan zona waktu UTC
            val localDateTime = LocalDateTime.parse(it, formatter)
            val instant = localDateTime.toInstant(ZoneId.of("UTC") as ZoneOffset?)
            Timestamp(Date.from(instant))
        }
    }
}