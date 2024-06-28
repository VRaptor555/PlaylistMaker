package com.example.playlistmaker.api

import retrofit2.Call
import retrofit2.http.*

interface TracksApi {
    @GET("search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}