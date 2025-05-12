package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.search.domain.db.FavoriteInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    application: Application,
) : AndroidViewModel(application), KoinComponent {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = playerState

    fun initPlayer() {
        renderState(PlayerState.Default())
        viewModelScope.launch {
            playerInteractor.prepareUrl(track.previewUrl)
            while (playerInteractor.state() != PlayerRepositoryImpl.STATE_PREPARED) {
                delay(10L)
            }
            renderState(PlayerState.Prepared(track.isFavorite))
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.state() == PlayerRepositoryImpl.STATE_PLAYING) {
                delay(DALAY_TIMER)
                playerState.postValue(PlayerState.Playing(getCurrentPosition(), track.isFavorite))
            }
            playerState.postValue(PlayerState.Prepared(track.isFavorite))
        }
    }

    fun play() {
        playerInteractor.playback()
        showCurrentStatus()
    }

    private fun showCurrentStatus() {
        when (playerInteractor.state()) {
            PlayerRepositoryImpl.STATE_PLAYING -> startTimer()
            PlayerRepositoryImpl.STATE_PAUSED -> {
                timerJob?.cancel()
                playerState.postValue(PlayerState.Paused(getCurrentPosition(), track.isFavorite))
            }

            PlayerRepositoryImpl.STATE_PREPARED -> renderState(PlayerState.Prepared(track.isFavorite))
            PlayerRepositoryImpl.STATE_DEFAULT -> renderState(PlayerState.Default())
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerInteractor.currentPosition()) ?: "00:00"
    }

    private fun renderState(state: PlayerState) {
        playerState.postValue(state)
    }

    companion object {
        private const val DALAY_TIMER = 300L
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.delTrack(track)
                track.isFavorite = false
            } else {
                favoriteInteractor.addTrack(track)
                track.isFavorite = true
            }
            showCurrentStatus()
        }
    }

    fun onAddToPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addTrackToPlaylist(playlist, track)
        }
    }

}