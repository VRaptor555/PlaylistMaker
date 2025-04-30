package com.example.playlistmaker.library.db

import com.example.playlistmaker.library.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlaylistDbConverter,
): PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistEntity = converter.map(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistEntity = converter.map(playlist)
        appDatabase.playlistDao().deletePlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getAllPlaylist()
        emit(convertFromEntityList(playlists))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = converter.map(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    private fun convertFromEntityList(playlistEntitys: List<PlaylistEntity>): List<Playlist> {
        return playlistEntitys.map {
            playlist -> converter.map(playlist)
        }
    }
}