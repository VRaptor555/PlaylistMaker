package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    fun addToSavedTracksList(track: Track)
    fun clearTracks()
    fun saveTracksList()
    fun getSavedTracksList(): MutableList<Track>
    fun size(): Int
}