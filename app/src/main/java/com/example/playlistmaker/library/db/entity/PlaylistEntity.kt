package com.example.playlistmaker.library.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.library.db.converters.ListConverter

@Entity(
    tableName = "playlists",
    indices = [Index(value = ["namePlaylist"], unique = true)]
)
@TypeConverters(ListConverter::class)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "namePlaylist")
    val namePlaylist: String,
    val description: String?,
    val imagePath: String?,
    val tracksId: List<Long>,
    val countTracks: Int
)
