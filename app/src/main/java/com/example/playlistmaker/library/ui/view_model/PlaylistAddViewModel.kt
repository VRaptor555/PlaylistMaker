package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.db.PlaylistInteractor

class PlaylistAddViewModel(
    private val playlistInteractor: PlaylistInteractor,
): ViewModel() {

}