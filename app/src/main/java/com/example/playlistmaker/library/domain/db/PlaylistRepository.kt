package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
}