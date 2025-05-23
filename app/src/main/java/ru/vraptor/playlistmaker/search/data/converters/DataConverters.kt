package ru.vraptor.playlistmaker.search.data.converters

import androidx.room.TypeConverter
import java.util.Date

class DataConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? =
        value?.let { Date(it) }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? =
        date?.time
}