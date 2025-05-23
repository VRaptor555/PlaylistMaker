package ru.vraptor.playlistmaker.search.domain

import ru.vraptor.playlistmaker.search.domain.model.Track

interface HistoryRepository {
    suspend fun read(): List<Track>
    fun write(writeRecord: Track)
    fun clear()
}