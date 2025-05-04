package com.example.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState
import kotlinx.coroutines.launch

class PlaylistCreateViewModel(
    private val playlistInteractor: PlaylistInteractor,
): ViewModel() {
    private var playlistName = ""
    private var playlistImage: String? = null
    private var playlistDescription: String? = null

    private val stateLiveData = MutableLiveData<PlaylistCreateState>()
    fun observeState(): LiveData<PlaylistCreateState> = stateLiveData

    init {
        stateLiveData.postValue(PlaylistCreateState.Filled(false))
    }

    fun changeName(name: String?) {
        playlistName = name ?: ""
        if (playlistName.isEmpty()) {
            stateLiveData.postValue(PlaylistCreateState.Filled(false))
        } else {
            stateLiveData.postValue(PlaylistCreateState.Filled(true))
        }
    }

    fun changeDescription(description: String?) {
        playlistDescription = description
    }

    fun changeImage(uri: Uri) {
        playlistImage = uri.toString()
        stateLiveData.postValue(PlaylistCreateState.ChangeImage(uri))
    }

    fun savePlaylistToDB() {
        if (playlistName.isNotEmpty()) {
            val playlist = Playlist(
                id = 0,
                name = playlistName,
                description = playlistDescription,
                imagePath = playlistImage,
                tracksId = listOf(),
                countTracks = 0
            )
            viewModelScope.launch {
                playlistInteractor.addPlaylist(playlist)
            }
        }
    }

}