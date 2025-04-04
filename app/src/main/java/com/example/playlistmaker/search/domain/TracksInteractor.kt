package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(text: String): Flow<Pair<List<Track>?, String?>>
}