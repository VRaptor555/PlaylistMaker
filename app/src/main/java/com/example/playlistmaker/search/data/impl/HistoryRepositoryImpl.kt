package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.db.AppDatabase
import com.example.playlistmaker.search.domain.HistoryRepository
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.domain.model.Track

class HistoryRepositoryImpl(
    private val localStorage: TracksListHistoryStorage,
    private val appDatabase: AppDatabase,
): HistoryRepository {
    override suspend fun read(): List<Track> {
        val historyTracks = localStorage.getSavedList()
        if (historyTracks.isEmpty()) { return historyTracks}
        val favoriteList = appDatabase.favoriteDao().getIdFavorite()
        for (track in historyTracks) {
            track.isFavorite = track.trackId in favoriteList
        }
        return historyTracks
    }

    override fun write(writeRecord: Track) {
        localStorage.addToHistoryList(writeRecord)
    }

    override fun clear() {
        localStorage.clearHistoryList()
    }
}