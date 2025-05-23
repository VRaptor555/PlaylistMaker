package ru.vraptor.playlistmaker.search.data.db

import ru.vraptor.playlistmaker.search.data.converters.TrackDbConverter
import ru.vraptor.playlistmaker.search.data.db.entity.FavoriteEntity
import ru.vraptor.playlistmaker.search.domain.db.FavoriteRepository
import ru.vraptor.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter

) : FavoriteRepository {
    override suspend fun addTrack(track: Track) {
        val favoriteEntities = trackDbConverter.map(track)
        appDatabase.favoriteDao().insertToFavorite(favoriteEntities)
    }

    override suspend fun delTrack(track: Track) {
        val favoriteEntities = trackDbConverter.map(track)
        appDatabase.favoriteDao().deleteFromFavorite(favoriteEntities)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val favorites = appDatabase.favoriteDao().getAllFavorite()
        emit(convertFromEntities(favorites))
    }

    private fun convertFromEntities(tracks: List<FavoriteEntity>): List<Track> {
        return tracks.map {
            track -> trackDbConverter.map(track)
        }
    }
}