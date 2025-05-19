package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistDetailState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
): ViewModel() {
    private var tracksList: MutableList<Track> =  mutableListOf()
    private var _playlist: Playlist? = null

    init {
        _playlist = playlist
    }

    private val stateLiveData = MutableLiveData<PlaylistDetailState>()
    fun observeState(): LiveData<PlaylistDetailState> = stateLiveData

    fun showContext() {
        stateLiveData.postValue(PlaylistDetailState.Loading(_playlist!!))
        viewModelScope.launch {
            playlistInteractor.getTracksFromPlaylist(_playlist!!.tracksId)
                .collect { tracks ->
                    tracksList.clear()
                    tracksList.addAll(tracks)
                    stateLiveData.postValue(PlaylistDetailState.Context(tracks))
                }
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            _playlist = playlistInteractor.deleteTrackFromPlaylist(_playlist!!, track)
            showContext()
        }
    }
}