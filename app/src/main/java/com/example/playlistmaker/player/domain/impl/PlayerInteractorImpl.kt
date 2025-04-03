package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.PlayerInteractor
import org.koin.core.component.KoinComponent

class PlayerInteractorImpl(
    private val repository: PlayerRepository
): PlayerInteractor, KoinComponent {
    override fun prepareUrl(url: String?) {
        repository.initUrlPreview(url)
    }

    override fun currentPosition(): Int {
        return repository.currentTime()
    }

    override fun playback() {
        repository.playback()
    }

    override fun state(): Int {
        return repository.state()
    }
}