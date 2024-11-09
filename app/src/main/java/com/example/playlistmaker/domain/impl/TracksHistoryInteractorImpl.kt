package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.models.Track

class TracksHistoryInteractorImpl(private val sharedHistory: HistoryRepository): TracksHistoryInteractor {
    private var tracks: MutableList<Track> = sharedHistory.read().toMutableList()

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
        sharedHistory.write(tracks.toTypedArray())
    }

    override fun getSavedTracksList(): MutableList<Track> {
        return tracks
    }

    override fun size(): Int {
        return tracks.size
    }
}