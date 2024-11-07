package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    val tracks: MutableList<Track>

    fun addToSavedTracksList(track: Track)
    fun clearTracks()
    fun saveTracksList()
}