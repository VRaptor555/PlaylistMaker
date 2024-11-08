package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun preparePlayer(consumer: PlayerConsumer)

    interface PlayerConsumer {
        fun showTime(currentPosition: Int, currentState: Int)
    }
    fun playback()
    fun state(): Int
}