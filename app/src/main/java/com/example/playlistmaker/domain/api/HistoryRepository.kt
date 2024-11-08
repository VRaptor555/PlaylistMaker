package com.example.playlistmaker.domain.api

interface HistoryRepository {
    fun read(): Any
    fun write(writeList: Any)
}