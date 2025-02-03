package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(consumer: PlayerConsumer)
    fun playback()
    fun state(): Int
}