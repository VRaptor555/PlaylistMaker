package com.example.playlistmaker.player.ui.view_model

import android.app.Application
import android.widget.Toast
import androidx.core.bundle.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.search.domain.db.FavoriteInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application,
) : AndroidViewModel(application), KoinComponent {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default(track))
    fun observeState(): LiveData<PlayerState> = playerState

    fun initPlayer() {
        renderState(PlayerState.Default(track))
        viewModelScope.launch {
            playerInteractor.prepareUrl(track.previewUrl)
            while (playerInteractor.state() != PlayerRepositoryImpl.STATE_PREPARED) {
                delay(10L)
            }
            renderState(PlayerState.Prepared(track.isFavorite))
        }
    }

    private val playlistState = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

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
            PlayerRepositoryImpl.STATE_DEFAULT -> renderState(PlayerState.Default(track))
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

    fun onFavoriteClicked(analytics: FirebaseAnalytics) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.delTrack(track)
                track.isFavorite = false
            } else {
                favoriteInteractor.addTrack(track)
                track.isFavorite = true
                val bundle = Bundle()
                bundle.putSerializable("Add to favorite", track)
                analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            }
            showCurrentStatus()
        }
    }

    suspend fun onAddToPlaylist(playlist: Playlist): Boolean {
        val isAddToPlaylist = playlistInteractor.addTrackToPlaylist(playlist, track)
        if (isAddToPlaylist) {
            Toast.makeText(application,
                String.format(application.getString(R.string.track_added_playlist), playlist.name),
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(application,
                String.format(application.getString(R.string.track_in_playlist), playlist.name),
                Toast.LENGTH_SHORT).show()
        }
        return isAddToPlaylist
    }

    fun playlistLoad() {
        playlistState.postValue(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlist ->
                playlistState.postValue(PlaylistState.Content(playlist))
            }
        }
    }

}