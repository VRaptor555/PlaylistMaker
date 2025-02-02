package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.HistoryRepository
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.domain.model.Track

class HistoryRepositoryImpl(
    private val localStorage: TracksListHistoryStorage,
): HistoryRepository {
    override fun read(): Array<Track> {
        return localStorage.getSavedList().toTypedArray()
    }

    override fun write(writeRecord: Track) {
        localStorage.addToHistoryList(writeRecord)
    }

    override fun clear() {
        localStorage.clearHistoryList()
    }
}