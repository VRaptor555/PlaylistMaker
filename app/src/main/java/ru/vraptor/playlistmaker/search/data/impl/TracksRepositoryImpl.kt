package ru.vraptor.playlistmaker.search.data.impl

import android.util.Log
import ru.vraptor.playlistmaker.search.data.TracksRequest
import ru.vraptor.playlistmaker.search.data.TracksSearchRequest
import ru.vraptor.playlistmaker.search.data.TracksSearchResponse
import ru.vraptor.playlistmaker.search.data.db.AppDatabase
import ru.vraptor.playlistmaker.search.data.network.NetworkClient
import ru.vraptor.playlistmaker.search.domain.TracksRepository
import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) : TracksRepository {
    override fun searchTracks(text: String): Flow<Resource<List<Track>>> {
        val request = TracksSearchRequest(text)
        return getTracks(request)
    }

    private fun getTracks(request: TracksRequest): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(request)
        Log.d("PLAYLIST_TEST", "ResultCode: ${response.resultCode}")
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))
            200 -> {
                with(response as TracksSearchResponse) {
                    val favoriteList = appDatabase.favoriteDao().getIdFavorite()
                    val data = results.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            previewUrl = it.previewUrl,
                            isFavorite = it.trackId in favoriteList
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }
}