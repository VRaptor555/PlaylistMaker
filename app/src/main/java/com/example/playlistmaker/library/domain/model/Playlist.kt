package com.example.playlistmaker.library.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val tracksId: List<Long>,
    val countTracks: Int
)
