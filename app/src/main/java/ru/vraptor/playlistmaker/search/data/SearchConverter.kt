package ru.vraptor.playlistmaker.search.data

import ru.vraptor.playlistmaker.search.domain.model.Track

object SearchConverter {
    fun trackToDto(trackArray: List<Track>): List<TrackDto> {
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
        }
    }

    fun dtoToTrack(trackDtoArray: Array<TrackDto>): List<Track> {
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
        }
    }

}