package ru.vraptor.playlistmaker.search.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.vraptor.playlistmaker.search.data.db.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorite(favoriteTrack: FavoriteEntity)

    @Delete
    suspend fun deleteFromFavorite(favoriteTrack: FavoriteEntity)

    @Query("SELECT * FROM favorite_tracks ORDER BY dateAdds DESC")
    suspend fun getAllFavorite(): List<FavoriteEntity>

    @Query("SELECT id FROM favorite_tracks")
    suspend fun getIdFavorite(): List<Long>
}