package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Resource

interface TracksRepository {
    fun searchTracks(text: String): Resource<List<Track>>
}