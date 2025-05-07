package com.example.playlistmaker.search.data.converters

import com.example.playlistmaker.search.data.db.entity.FavoriteEntity
import com.example.playlistmaker.search.domain.model.Track
import java.util.Date

class TrackDbConverter {
    fun map(track: Track): FavoriteEntity {
        return FavoriteEntity(track.trackId, track.previewUrl, track.trackName, track.artistName,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.trackTimeMillis,
            track.artworkUrl100, Date())
    }

    fun map(favorite: FavoriteEntity): Track {
        return Track(favorite.id, favorite.trackName, favorite.artistName, favorite.collectionName,
            favorite.releaseDate, favorite.primaryGenreName, favorite.country, favorite.trackTimeMillis,
            favorite.artworkUrl100, favorite.previewUrl, true)
    }
}