package com.example.playlistmaker.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.sqlite.SQLiteException
import com.example.playlistmaker.library.db.entity.PlaylistEntity
import kotlin.jvm.Throws

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.ABORT)
    @Throws(SQLiteException::class)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Query("SELECT countTracks FROM playlists WHERE id = :idPlaylist")
    suspend fun getCountTracks(idPlaylist: Long): Int

    @Query("SELECT * FROM playlists WHERE id = :idPlaylist")
    suspend fun getPlaylistById(idPlaylist: Long): PlaylistEntity
}