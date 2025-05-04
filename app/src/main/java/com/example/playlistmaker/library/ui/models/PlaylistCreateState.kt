package com.example.playlistmaker.library.ui.models

import android.net.Uri
import com.example.playlistmaker.library.domain.model.Playlist

sealed interface PlaylistCreateState {
    data object Cancel: PlaylistCreateState

    data class ChangeImage(
        val imagePath: Uri
    ): PlaylistCreateState
    data class Filled(
        val filled: Boolean
    ): PlaylistCreateState
    data class SavePlaylist(
        val playlist: Playlist
    ): PlaylistCreateState
}