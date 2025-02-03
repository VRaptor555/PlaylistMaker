package com.example.playlistmaker.player.domain

interface PlayerConsumer {
    fun showTime(currentPosition: Int, currentState: Int)
}