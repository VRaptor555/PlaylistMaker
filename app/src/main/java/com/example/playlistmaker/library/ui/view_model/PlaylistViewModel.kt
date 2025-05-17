package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
): ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    init {
        showPlaylists()
    }

    fun showPlaylists() {
        stateLiveData.postValue(PlaylistState.Loading)
        viewModelScope.launch {
            playlistInteractor.getPlaylists()
                .collect { playlist ->
                    showContent(playlist)
                }
        }
    }

    private fun showContent(playlist: List<Playlist>) {
        if (playlist.isEmpty()) {
            stateLiveData.postValue(PlaylistState.Empty)
        } else {
            stateLiveData.postValue(PlaylistState.Content(playlist))
        }
    }
}