package com.example.playlistmaker.player.data


interface PlayerRepository {
    fun playback()
    fun currentTime(): Int
    fun state(): Int
}