package ru.vraptor.playlistmaker.library.ui.models

import ru.vraptor.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistState {
    data object Loading: PlaylistState
    data object Empty: PlaylistState

    data class Content(
        val playlist: List<Playlist>
    ): PlaylistState

    data class Error(
        val errorMessage: String
    ): PlaylistState

}