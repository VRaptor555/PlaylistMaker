package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerConsumer

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(consumer: PlayerConsumer) {
        consumer.showTime(repository.currentTime(), repository.state())
    }

    override fun playback() {
        repository.playback()
    }

    override fun state(): Int {
        return repository.state()
    }
}