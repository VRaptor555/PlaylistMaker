package com.example.playlistmaker.presentation

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.History
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class TracksSearchHistoryImpl(val sharedHistory: History): TracksHistoryInteractor {
    override var tracks: MutableList<Track>

    init {
        val list = sharedHistory.read() as Array<*>
        if (list.isArrayOf<TrackDto>()) {
        tracks = (list as Array<TrackDto>).map {
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
        }.toCollection(ArrayList())
        } else {
            tracks = emptyList<Track>().toCollection(ArrayList())
        }
    }

    override fun addToSavedTracksList(track: Track){
        val findIndex = tracks.indexOfFirst { it.trackId == track.trackId }
        if (findIndex != -1){
            tracks.removeAt(findIndex)
        }
        tracks.add(0, track)
        if (tracks.size > 10)
            tracks.removeLast()
    }

    override fun clearTracks(){
        tracks.clear()
    }


    override fun saveTracksList(){
        sharedHistory.write(tracks.map {
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
        }.toTypedArray())
    }
}