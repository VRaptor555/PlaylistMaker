package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface TracksConsumer {
    fun consume(foundTracks: List<Track>?, errorMessage: String?)
}