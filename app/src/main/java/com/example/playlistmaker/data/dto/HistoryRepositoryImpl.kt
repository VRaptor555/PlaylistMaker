package com.example.playlistmaker.data.dto

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class HistoryRepositoryImpl(private val shared: SharedPreferences, private val historyKey: String): HistoryRepository {
    override fun read(): Array<Track> {
        val json = shared.getString(historyKey, null) ?: return emptyArray<Track>()
        return getTrackList(Gson().fromJson(json, Array<TrackDto>::class.java))
    }

    override fun write(writeList: Any) {
        if (writeList is Array<*> && writeList.isArrayOf<TrackDto>()) {
            val json = Gson().toJson(writeList)
            shared.edit()
                .putString(historyKey, json)
                .apply()
        }
    }

    private fun getTrackList(tracklist: Array<TrackDto>): Array<Track> {
        return tracklist.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl
            )
        }.toTypedArray()
    }
}