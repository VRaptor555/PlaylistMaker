package com.example.playlistmaker.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.History
import com.google.gson.Gson

class HistoryRepository(val shared: SharedPreferences, val historyKey: String): History {
    override fun read(): Any {
        val json = shared.getString(historyKey, null) ?: return emptyArray<TrackDto>()
        return Gson().fromJson(json, Array<TrackDto>::class.java)
    }

    override fun write(writeList: Any) {
        if (writeList is Array<*> && writeList.isArrayOf<TrackDto>()) {
            val json = Gson().toJson(writeList)
            shared.edit()
                .putString(historyKey, json)
                .apply()
        }
    }
}