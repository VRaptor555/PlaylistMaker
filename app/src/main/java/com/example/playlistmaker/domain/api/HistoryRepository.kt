package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun read(): Array<Track>
    fun write(writeList: Array<Track>)
}