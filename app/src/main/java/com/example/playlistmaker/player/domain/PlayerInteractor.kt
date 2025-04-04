package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun prepareUrl(url: String?)
    fun playback()
    fun currentPosition(): Int
    fun state(): Int
}