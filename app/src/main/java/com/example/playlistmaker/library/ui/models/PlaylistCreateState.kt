package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistCreateState {
    data class ChangePlaylist(
        val playlist: Playlist
    ): PlaylistCreateState
}