package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerConsumer
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.utils.timeMillisToMin
import org.koin.core.component.KoinComponent

class PlayerViewModel(
    private val urlPreview: String?,
    private val playerInteractor: PlayerInteractor,
    application: Application,
): AndroidViewModel(application), KoinComponent {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerState>()
    private val mediatorTracksStateLiveData = MediatorLiveData<PlayerState>().also { liveData ->
        liveData.addSource(stateLiveData) { playerState ->
            liveData.value = when(playerState) {
                is PlayerState.IsPlay -> PlayerState.IsPlay(playerState.timeCode)
                is PlayerState.IsPause -> PlayerState.IsPause(playerState.timeCode)
                is PlayerState.Loading -> playerState
            }
        }
    }
    fun observeState(): LiveData<PlayerState> = mediatorTracksStateLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    fun initPlayer() {
        playerInteractor.setUrl(urlPreview)
        renderState(PlayerState.Loading)
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    setCurrentPosition()
                    handler.postDelayed(this, DALAY_TIMER)
                }
            },
            DALAY_TIMER
        )
    }

    fun play() {
        playerInteractor.playback()
    }

    private fun setCurrentPosition() {
        playerInteractor.preparePlayer(object : PlayerConsumer {
            override fun showTime(currentPosition: Int, currentState: Int) {
                when (currentState) {
                    PlayerRepositoryImpl.STATE_PLAYING -> {
                        renderState(PlayerState.IsPlay(timeMillisToMin(currentPosition)))
                    }
                    PlayerRepositoryImpl.STATE_DEFAULT ->
                        renderState(PlayerState.Loading)
                    PlayerRepositoryImpl.STATE_PREPARED -> {
                        renderState(PlayerState.IsPause("0:00"))
                    }
                    PlayerRepositoryImpl.STATE_PAUSED -> {
                        renderState(PlayerState.IsPause(timeMillisToMin(currentPosition)))
                    }
                }
            }
        })
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val DALAY_TIMER = 250L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}