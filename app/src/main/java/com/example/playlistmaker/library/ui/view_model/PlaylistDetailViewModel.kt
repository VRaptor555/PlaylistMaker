package com.example.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistDetailState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.utils.timeMillisToMin
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
    private val shareInteractor: SharingInteractor,
    private val application: Application
) : ViewModel() {
    private var tracksList: MutableList<Track> = mutableListOf()

    private val stateLiveData = MutableLiveData<PlaylistDetailState>()
    fun observeState(): LiveData<PlaylistDetailState> = stateLiveData

    val myPlaylist get() = playlist

    fun loadingPlaylist() {
        stateLiveData.postValue(PlaylistDetailState.Loading)
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(playlist.tracksId)
                .collect { tracks ->
                    tracksList.clear()
                    tracksList.addAll(tracks)
                    stateLiveData.postValue(PlaylistDetailState.Content(playlist, tracks))
                }
        }
    }

    fun showPlaylist() {
        stateLiveData.postValue(PlaylistDetailState.ShowPlaylist(playlist))
    }

    suspend fun updatePlaylist() {
        playlistInteractor.getPlaylistById(playlist.id).collect { playlistnew ->
            playlist = playlistnew
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlist = playlistInteractor.deleteTrackFromPlaylist(playlist, track)
            loadingPlaylist()
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            for (track in tracksList) {
                playlist = playlistInteractor.deleteTrackFromPlaylist(playlist, track)
            }
            playlistInteractor.deletePlaylist(playlist)
            stateLiveData.postValue(PlaylistDetailState.ToExit)
        }
    }

    fun sharePlaylist() {
        if (tracksList.isEmpty()) {
            stateLiveData.postValue(PlaylistDetailState.SendMessage(application.getString(R.string.playlist_empty_track)))
        } else {
            var message = playlist.name + "\n"
            playlist.description?.let {
                if (it.isNotEmpty()) {
                    message += playlist.description + "\n"
                }
            }
            val countTrackStr = String.format(
                application.getString(R.string.playlist_count_tracks),
                playlist.countTracks
            )
            message += countTrackStr + "\n"
            var numberTrack = 1
            for (track in tracksList) {
                message += "${numberTrack}. ${track.artistName} - ${track.trackName} (${
                    timeMillisToMin(
                        track.trackTimeMillis
                    )
                })\n"
                numberTrack++
            }
            stateLiveData.postValue(
                PlaylistDetailState.ShareIntent(
                    shareInteractor.share(
                        message, application.getString(R.string.playlist_tab)
                    )
                )
            )
        }
    }
}