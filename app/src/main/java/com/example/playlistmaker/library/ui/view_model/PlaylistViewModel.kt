package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.ui.models.PlaylistState

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
): ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    init {
        stateLiveData.postValue(PlaylistState.Empty)
    }
}