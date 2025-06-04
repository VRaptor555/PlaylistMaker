package ru.vraptor.playlistmaker.library.domain.model

import java.io.Serializable

data class Playlist(
    var id: Long,
    var name: String,
    var description: String?,
    var imagePath: String?,
    var tracksId: List<Long>,
    var countTracks: Int
): Serializable
