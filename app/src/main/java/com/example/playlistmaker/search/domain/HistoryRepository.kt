package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface HistoryRepository {
    fun read(): Array<Track>
    fun write(writeRecord: Track)
    fun clear()
}