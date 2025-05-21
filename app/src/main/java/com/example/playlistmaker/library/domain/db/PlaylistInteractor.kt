package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist): Long
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistById(id: Long): Flow<Playlist>
    fun getTracksFromPlaylist(tracksId: List<Long>): Flow<List<Track>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean
    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track): Playlist
    suspend fun updatePlaylist(playlist: Playlist)
}