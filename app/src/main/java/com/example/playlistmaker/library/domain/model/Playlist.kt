package com.example.playlistmaker.library.domain.model

data class Playlist(
    val id: Long,
    var name: String,
    var description: String?,
    var imagePath: String?,
    val tracksId: List<Long>,
    var countTracks: Int
)
