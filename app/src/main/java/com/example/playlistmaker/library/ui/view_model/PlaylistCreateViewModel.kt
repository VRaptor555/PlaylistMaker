package com.example.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.SQLiteException
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState

open class PlaylistCreateViewModel(
    open var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistCreateState>()
    fun observeState(): LiveData<PlaylistCreateState> = stateLiveData

    fun loadPlaylist() {
        stateLiveData.postValue(PlaylistCreateState.ChangePlaylist(playlist))
    }

    fun changeName(name: String?) {
        playlist.name = name ?: ""
        loadPlaylist()
    }

    fun changeDescription(description: String?) {
        playlist.description = description
        loadPlaylist()
    }

    fun changeImage(uri: Uri) {
        playlist.imagePath = uri.toString()
        loadPlaylist()
    }

    open suspend fun savePlaylistToDb(): Boolean {
        try {
            val newId = playlistInteractor.addPlaylist(playlist)
            playlist.id = newId
            return true
        } catch (_: SQLiteException) {
            return false
        }
    }

}