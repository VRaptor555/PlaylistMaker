package ru.vraptor.playlistmaker.library.domain.db

import ru.vraptor.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist): Long
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylistById(idPlaylist: Long): Flow<Playlist>
}