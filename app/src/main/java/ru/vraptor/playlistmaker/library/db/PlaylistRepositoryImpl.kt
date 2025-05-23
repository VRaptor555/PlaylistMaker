package ru.vraptor.playlistmaker.library.db

import ru.vraptor.playlistmaker.library.db.converters.PlaylistDbConverter
import ru.vraptor.playlistmaker.library.db.entity.PlaylistEntity
import ru.vraptor.playlistmaker.library.domain.db.PlaylistRepository
import ru.vraptor.playlistmaker.library.domain.model.Playlist
import ru.vraptor.playlistmaker.search.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlaylistDbConverter,
): PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist): Long {
        val playlistEntity = converter.map(playlist)
        return appDatabase.playlistDao().insertPlaylist(playlistEntity)
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

    override fun getPlaylistById(idPlaylist: Long): Flow<Playlist> = flow {
        val playlist = appDatabase.playlistDao().getPlaylistById(idPlaylist)
        emit(converter.map(playlist))
    }

    private fun convertFromEntityList(playlistEntitys: List<PlaylistEntity>): List<Playlist> {
        return playlistEntitys.map {
            playlist -> converter.map(playlist)
        }
    }
}