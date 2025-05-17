package com.example.playlistmaker.library.db.converters

import com.example.playlistmaker.library.db.entity.TracklistEntity
import com.example.playlistmaker.search.domain.model.Track

class TracklistDbConverter {
    fun map(track: Track): TracklistEntity{
        return TracklistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl
        )
    }

    fun map(tracklist: TracklistEntity): Track{
        return Track(
            tracklist.trackId,
            tracklist.trackName,
            tracklist.artistName,
            tracklist.collectionName,
            tracklist.releaseDate,
            tracklist.primaryGenreName,
            tracklist.country,
            tracklist.trackTimeMillis,
            tracklist.artworkUrl100,
            tracklist.previewUrl
        )
    }
}