package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

object Converter {
    fun trackToDto(trackArray: Array<Track>): Array<TrackDto> {
        return trackArray.map {
            TrackDto(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl
            )
        }.toTypedArray()
    }

    fun dtoToTrack(trackDtoArray: Array<TrackDto>): Array<Track> {
        return trackDtoArray.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl
            )
        }.toTypedArray()
    }

}