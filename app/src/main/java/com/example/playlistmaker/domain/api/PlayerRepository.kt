package com.example.playlistmaker.domain.api


interface PlayerRepository {
    fun playback()
    fun currentTime(): Int
    fun state(): Int
}