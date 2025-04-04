package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val urlPreview: String?,
    private val playerInteractor: PlayerInteractor,
    application: Application,
): AndroidViewModel(application), KoinComponent {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = playerState

    fun initPlayer() {
        renderState(PlayerState.Default())
        viewModelScope.launch {
            playerInteractor.prepareUrl(urlPreview)
            while (playerInteractor.state() != PlayerRepositoryImpl.STATE_PREPARED) {
                delay(10L)
            }
            renderState(PlayerState.Prepared())
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.state() == PlayerRepositoryImpl.STATE_PLAYING) {
                delay(DALAY_TIMER)
                playerState.postValue(PlayerState.Playing(getCurrentPosition()))
            }
            playerState.postValue(PlayerState.Prepared())
        }
    }

    fun play() {
        playerInteractor.playback()
        when(playerInteractor.state()) {
            PlayerRepositoryImpl.STATE_PLAYING -> startTimer()
            PlayerRepositoryImpl.STATE_PAUSED -> {
                timerJob?.cancel()
                playerState.postValue(PlayerState.Paused(getCurrentPosition()))
            }
            PlayerRepositoryImpl.STATE_PREPARED -> renderState(PlayerState.Prepared())
            PlayerRepositoryImpl.STATE_DEFAULT -> renderState(PlayerState.Default())
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.currentPosition()) ?: "00:00"
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    companion object {
        private const val DALAY_TIMER = 300L
    }

}