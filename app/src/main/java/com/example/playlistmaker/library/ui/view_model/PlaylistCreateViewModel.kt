package com.example.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.SQLiteException
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState

class PlaylistCreateViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {
    private var playlist: Playlist = Playlist(
        0,
        "",
        null,
        null,
        listOf(),
        0
    )

    private val stateLiveData = MutableLiveData<PlaylistCreateState>()
    fun observeState(): LiveData<PlaylistCreateState> = stateLiveData

    init {
        stateLiveData.postValue(PlaylistCreateState.ChangePlaylist(playlist))
    }

    fun changeName(name: String?) {
        playlist.name = name ?: ""
        stateLiveData.postValue(PlaylistCreateState.ChangePlaylist(playlist))
    }

    fun changeDescription(description: String?) {
        playlist.description = description
    }

    fun changeImage(uri: Uri) {
        playlist.imagePath = uri.toString()
        stateLiveData.postValue(PlaylistCreateState.ChangePlaylist(playlist))
    }

    suspend fun savePlaylistToDb(): Boolean {
        try {
            playlistInteractor.addPlaylist(playlist)
            return true
        } catch (_: SQLiteException) {
            return false
        }
    }

}