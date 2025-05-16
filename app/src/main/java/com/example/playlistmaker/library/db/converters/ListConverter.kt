package com.example.playlistmaker.library.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListConverter {
    val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<Long>? {
        if (value == null || value.isEmpty()) {
            return listOf<Long>()
        }
        return gson.fromJson(value, Array<Long>::class.java).toMutableList()
    }
    @TypeConverter
    fun fromList(value: List<Long>?): String? {
        if (value == null || value.isEmpty()) {
            return ""
        }
        return gson.toJson(value)
    }
}