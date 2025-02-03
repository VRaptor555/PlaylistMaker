package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface TracksInteractor {
    fun searchTracks(text: String, consumer: TracksConsumer)
}