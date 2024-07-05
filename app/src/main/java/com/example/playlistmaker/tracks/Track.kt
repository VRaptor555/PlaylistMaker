package com.example.playlistmaker.tracks

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
)
