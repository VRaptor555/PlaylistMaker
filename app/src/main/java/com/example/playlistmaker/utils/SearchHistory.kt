package com.example.playlistmaker.utils

import android.content.SharedPreferences
import com.example.playlistmaker.ui.tracks.SEARCH_HISTORY_KEY
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistory(val shared: SharedPreferences) {
    var tracks: MutableList<Track>

    init {
        tracks = read().toCollection(ArrayList())
    }

    fun addToSavedTrackList(track: Track){
        val findIndex = tracks.indexOfFirst { it.trackId == track.trackId }
        if (findIndex != -1){
            tracks.removeAt(findIndex)
        }
        tracks.add(0, track)
        if (tracks.size > 10)
            tracks.removeLast()
    }

    fun clearTracks(){
        tracks.clear()
    }

    private fun read(): Array<Track> {
        val json = shared.getString(SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun write(){
        val json = Gson().toJson(tracks)
        shared.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun saveSearchingList(){
        write()
    }
}