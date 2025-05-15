package com.example.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.db.dao.PlaylistDao
import com.example.playlistmaker.library.db.dao.TracklistDao
import com.example.playlistmaker.library.db.entity.PlaylistEntity
import com.example.playlistmaker.library.db.entity.TracklistEntity
import com.example.playlistmaker.search.data.db.dao.FavoriteDao
import com.example.playlistmaker.search.data.db.entity.FavoriteEntity

@Database(
    version = 1,
    entities = [
        FavoriteEntity::class,
        PlaylistEntity::class,
        TracklistEntity::class
    ],
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracklistDao(): TracklistDao
}