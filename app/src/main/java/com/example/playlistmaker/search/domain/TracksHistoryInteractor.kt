package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface TracksHistoryInteractor {
    fun addToSavedTracksList(track: Track)
    fun clearTracks()
    suspend fun getSavedTracksList(): MutableList<Track>
    suspend fun size(): Int
}