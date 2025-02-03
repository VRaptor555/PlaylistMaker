package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.TracksSearchRequest
import com.example.playlistmaker.search.data.TracksSearchResponse
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.utils.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    ) : TracksRepository {
    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(text))
        return when (response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")
            200 -> Resource.Success((response as TracksSearchResponse).results.map {
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
                    previewUrl = it.previewUrl
                )
            }

            )
            else -> Resource.Error("Ошибка сервера")
        }
    }
}