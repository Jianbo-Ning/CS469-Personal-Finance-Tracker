package com.example.money_management.db

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTimestampToDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
