package ru.vraptor.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.vraptor.playlistmaker.search.data.TracksSearchResponse

interface ITunesApiService {
    @GET("search?entity=song")
    suspend fun searchTracks(@Query("term") text: String): TracksSearchResponse

}