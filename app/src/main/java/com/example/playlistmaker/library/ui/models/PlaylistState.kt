package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface PlaylistState {
    data object Loading: PlaylistState
    data object Empty: PlaylistState

    data class Content(
        val tracks: List<Track>
    ): PlaylistState

    data class Error(
        val errorMessage: String
    ): PlaylistState

}