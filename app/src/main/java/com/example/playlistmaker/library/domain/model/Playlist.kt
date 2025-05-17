package com.example.playlistmaker.library.domain.model

data class Playlist(
    var id: Long,
    var name: String,
    var description: String?,
    var imagePath: String?,
    var tracksId: List<Long>,
    var countTracks: Int
)
