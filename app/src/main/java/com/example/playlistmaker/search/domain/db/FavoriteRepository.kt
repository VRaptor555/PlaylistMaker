package com.example.playlistmaker.search.domain.db

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addTrack(track: Track)
    suspend fun delTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}