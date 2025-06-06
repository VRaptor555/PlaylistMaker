package ru.vraptor.playlistmaker.search.data.dto

import android.content.SharedPreferences
import ru.vraptor.playlistmaker.search.data.SearchConverter
import ru.vraptor.playlistmaker.search.data.TrackDto
import ru.vraptor.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import androidx.core.content.edit

class TracksListHistoryStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {


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
        return SearchConverter.dtoToTrack(gson.fromJson(json, Array<TrackDto>::class.java))
    }

    private fun changeSavedList(tracks: List<Track>) {
        val json = gson.toJson(SearchConverter.trackToDto(tracks))
        sharedPreferences.edit() {
            putString(HISTORY_LIST_KEY, json)
        }
    }
}
