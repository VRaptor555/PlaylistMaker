package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun setUrl(url: String?)
    fun preparePlayer(consumer: PlayerConsumer)
    fun playback()
    fun state(): Int
}