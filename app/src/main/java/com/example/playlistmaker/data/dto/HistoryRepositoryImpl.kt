package com.example.playlistmaker.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class HistoryRepositoryImpl(private val shared: SharedPreferences, private val historyKey: String): HistoryRepository {
    override fun read(): Array<Track> {
        val json = shared.getString(historyKey, null) ?: return emptyArray<Track>()
        return Converter.dtoToTrack(Gson().fromJson(json, Array<TrackDto>::class.java))
    }

    override fun write(writeList: Array<Track>) {
        val json = Gson().toJson(Converter.trackToDto(writeList))
        shared.edit()
            .putString(historyKey, json)
            .apply()
    }
}