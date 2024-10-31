package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface TracksApiService {
    @GET("search?entity=song")
    fun searchTracks(@Query("term") text: String): Call<TracksSearchResponse>
}