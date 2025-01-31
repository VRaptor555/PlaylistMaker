package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(consumer: PlayerConsumer)

    interface PlayerConsumer {
        fun showTime(currentPosition: Int, currentState: Int)
    }
    fun playback()
    fun state(): Int
}