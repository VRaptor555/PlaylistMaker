package ru.vraptor.playlistmaker.library.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks",

)
data class TracklistEntity(
    @PrimaryKey
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
)