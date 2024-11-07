package com.example.playlistmaker.domain.api

interface History {
    fun read(): Any
    fun write(writeList: Any)
}