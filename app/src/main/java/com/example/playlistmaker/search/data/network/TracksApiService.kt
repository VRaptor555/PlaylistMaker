package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface TracksApiService {
    @GET("search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<TracksSearchResponse>
}