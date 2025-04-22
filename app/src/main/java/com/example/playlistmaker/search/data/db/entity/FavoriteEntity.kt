package com.example.playlistmaker.search.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.search.data.converters.DataConverters
import java.util.Date

@Entity(tableName = "favorite_tracks") @TypeConverters(DataConverters::class)
data class FavoriteEntity(
    @PrimaryKey
    val id: Long,
    val previewUrl: String?,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val dateAdds: Date
)