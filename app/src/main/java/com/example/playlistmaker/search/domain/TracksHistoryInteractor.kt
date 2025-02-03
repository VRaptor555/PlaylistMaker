package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface TracksHistoryInteractor {
    fun addToSavedTracksList(track: Track)
    fun clearTracks()
    fun getSavedTracksList(): MutableList<Track>
    fun size(): Int
}