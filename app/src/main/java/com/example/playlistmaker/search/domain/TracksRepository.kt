package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(text: String): Flow<Resource<List<Track>>>
}