package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracklistRepository {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTrackById(idTrack: Long): Flow<Track?>
    fun getTrackListById(idList: List<Long>): Flow<List<Track>>
}