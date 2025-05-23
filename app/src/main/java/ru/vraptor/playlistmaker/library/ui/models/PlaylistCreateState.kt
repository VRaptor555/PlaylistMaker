package ru.vraptor.playlistmaker.library.ui.models

import ru.vraptor.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistCreateState {
    data class ChangePlaylist(
        val playlist: Playlist
    ): PlaylistCreateState
}