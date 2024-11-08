package com.example.playlistmaker.data.dto

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer, url: String?): PlayerRepository {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private var state = STATE_DEFAULT

    init {
        url?.let {
            mediaPlayer.setDataSource(it)
            mediaPlayer.prepareAsync()
            state = STATE_PREPARED
            mediaPlayer.setOnCompletionListener {
                state = STATE_PREPARED
                state()
            }
            mediaPlayer.setOnPreparedListener {
                state = STATE_PREPARED
                state()
            }
        }
    }

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