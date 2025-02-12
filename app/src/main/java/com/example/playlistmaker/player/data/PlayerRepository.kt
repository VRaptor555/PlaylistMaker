package com.example.playlistmaker.player.data


interface PlayerRepository {
    fun initUrlPreview(urlPreview: String?)
    fun playback()
    fun currentTime(): Int
    fun state(): Int
}