package com.example.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val previewUrl: String?
): Serializable