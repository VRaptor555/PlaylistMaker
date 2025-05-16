package com.example.playlistmaker.library.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.db.entity.TracklistEntity

@Dao
interface TracklistDao {
    @Insert(entity = TracklistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TracklistEntity)

    @Delete
    suspend fun deleteTrack(track: TracklistEntity)

    @Query("SELECT * FROM tracks where trackId=:trackId")
    suspend fun getTrack(trackId: Long): TracklistEntity?

    @Query("SELECT * FROM tracks WHERE trackId IN (:idList)")
    suspend fun getTrackList(idList: List<Long>): List<TracklistEntity>
}