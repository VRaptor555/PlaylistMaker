package com.example.playlistmaker.library.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.library.db.converters.ListConverter

@Entity(tableName = "playlists") @TypeConverters(ListConverter::class)
data class PlaylistEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String?,
    val imagePath: String?,
    val tracksId: List<Long>,
    val countTracks: Int
)
