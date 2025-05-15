package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface HistoryRepository {
    suspend fun read(): List<Track>
    fun write(writeRecord: Track)
    fun clear()
}