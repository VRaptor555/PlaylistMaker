package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(consumer: PlayerInteractor.PlayerConsumer) {
        consumer.showTime(repository.currentTime(), repository.state())
    }

    override fun playback() {
        repository.playback()
    }

    override fun state(): Int {
        return repository.state()
    }
}