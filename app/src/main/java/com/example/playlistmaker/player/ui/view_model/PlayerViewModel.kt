package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.utils.timeMillisToMin

class PlayerViewModel(
    application: Application,
    urlPreview: String?,
): AndroidViewModel(application) {
    companion object {
        private const val DALAY_TIMER = 250L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(url: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[APPLICATION_KEY] as Application, url)
            }
        }
    }
    private val playerInteractor = Creator.providePlayerInteractor(urlPreview)

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
        playerInteractor.preparePlayer(object : PlayerInteractor.PlayerConsumer {
            override fun showTime(currentPosition: Int, currentState: Int) {
                when (currentState) {
                    PlayerRepositoryImpl.STATE_PLAYING -> {
                        renderState(PlayerState.IsPlay(timeMillisToMin(currentPosition)))
                    }
                    PlayerRepositoryImpl.STATE_PREPARED,
                    PlayerRepositoryImpl.STATE_DEFAULT -> {
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

}