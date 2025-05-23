package ru.vraptor.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vraptor.playlistmaker.library.db.dao.PlaylistDao
import ru.vraptor.playlistmaker.library.db.dao.TracklistDao
import ru.vraptor.playlistmaker.library.db.entity.PlaylistEntity
import ru.vraptor.playlistmaker.library.db.entity.TracklistEntity
import ru.vraptor.playlistmaker.search.data.db.dao.FavoriteDao
import ru.vraptor.playlistmaker.search.data.db.entity.FavoriteEntity

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