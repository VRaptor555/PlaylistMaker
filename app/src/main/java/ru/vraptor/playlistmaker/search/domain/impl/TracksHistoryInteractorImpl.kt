package ru.vraptor.playlistmaker.search.domain.impl

import ru.vraptor.playlistmaker.search.domain.HistoryRepository
import ru.vraptor.playlistmaker.search.domain.TracksHistoryInteractor
import ru.vraptor.playlistmaker.search.domain.model.Track

class TracksHistoryInteractorImpl(private val sharedHistory: HistoryRepository):
    TracksHistoryInteractor {

    override fun addToSavedTracksList(track: Track){
        sharedHistory.write(track)
    }

    override fun clearTracks(){
        sharedHistory.clear()
    }

    override suspend fun getSavedTracksList(): MutableList<Track> {
        return sharedHistory.read().toMutableList()
    }

    override suspend fun size(): Int {
        return sharedHistory.read().size
    }
}