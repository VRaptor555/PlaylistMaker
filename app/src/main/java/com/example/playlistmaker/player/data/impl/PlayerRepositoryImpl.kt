package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import org.koin.core.component.KoinComponent

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
): PlayerRepository, KoinComponent {

    override fun initUrlPreview(urlPreview: String?) {
        urlPreview?.let {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(it)
            mediaPlayer.prepareAsync()
            state = STATE_DEFAULT
            mediaPlayer.setOnCompletionListener {
                state = STATE_PREPARED
            }
            mediaPlayer.setOnPreparedListener {
                state = STATE_PREPARED
            }
        }
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private var state = STATE_DEFAULT

    override fun playback() {
        when (state) {
            STATE_PLAYING -> {
                mediaPlayer.pause()
                state = STATE_PAUSED
            }

            STATE_PREPARED, STATE_PAUSED -> {
                mediaPlayer.start()
                state = STATE_PLAYING
            }

            STATE_DEFAULT -> {
            }
        }
    }

    override fun currentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun state(): Int {
        return state
    }
}