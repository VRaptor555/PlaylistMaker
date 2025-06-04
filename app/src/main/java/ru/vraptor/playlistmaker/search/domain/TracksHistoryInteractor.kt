package ru.vraptor.playlistmaker.search.domain

import ru.vraptor.playlistmaker.search.domain.model.Track

interface TracksHistoryInteractor {
    fun addToSavedTracksList(track: Track)
    fun clearTracks()
    suspend fun getSavedTracksList(): MutableList<Track>
    suspend fun size(): Int
}