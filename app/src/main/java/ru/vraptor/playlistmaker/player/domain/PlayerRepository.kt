package ru.vraptor.playlistmaker.player.domain

interface PlayerRepository {
    fun initUrlPreview(urlPreview: String?)
    fun playback()
    fun currentTime(): Int
    fun state(): Int
}