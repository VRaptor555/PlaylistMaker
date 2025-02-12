package com.example.playlistmaker.search.data.dto

import android.content.Context
import com.example.playlistmaker.search.data.SearchConverter
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.PLAYLIST_MAKER_PREFERENCES
import com.google.gson.Gson

class TracksListHistoryStorage(context: Context) {
    val sharedPreferences = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)

    private companion object{
        const val HISTORY_LIST_KEY = "HISTORY_LIST"
    }

    fun addToHistoryList(track: Track) {
        val tracks = getSavedList().toMutableList()
        val findIndex = tracks.indexOfFirst { it.trackId == track.trackId }
        if (findIndex != -1){
            tracks.removeAt(findIndex)
        }
        tracks.add(0, track)
        if (tracks.size > 10)
            tracks.removeAt(tracks.lastIndex)
        changeSavedList(tracks)
    }

    fun clearHistoryList() {
        changeSavedList(emptyList<Track>())
    }

    fun getSavedList(): List<Track> {
        val json = sharedPreferences.getString(HISTORY_LIST_KEY, null) ?: return emptyList<Track>()
        return SearchConverter.dtoToTrack(Gson().fromJson(json, Array<TrackDto>::class.java))
    }

    private fun changeSavedList(tracks: List<Track>) {
        val json = Gson().toJson(SearchConverter.trackToDto(tracks))
        sharedPreferences.edit()
            .putString(HISTORY_LIST_KEY, json)
            .apply()
    }
}
