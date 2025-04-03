package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.model.Track

class TracksHistoryInteractorImpl(private val sharedHistory: HistoryRepository):
    TracksHistoryInteractor {

    override fun addToSavedTracksList(track: Track){
        sharedHistory.write(track)
    }

    override fun clearTracks(){
        sharedHistory.clear()
    }

    override fun getSavedTracksList(): MutableList<Track> {
        return sharedHistory.read().toMutableList()
    }

    override fun size(): Int {
        return sharedHistory.read().size
    }
}