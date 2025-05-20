package com.example.playlistmaker.library.ui.models

import android.content.Intent
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

sealed interface PlaylistDetailState {
    data class Loading(val playlist: Playlist): PlaylistDetailState
    data class Content(val tracks: List<Track>): PlaylistDetailState
    data class SendMessage(val message: String): PlaylistDetailState
    data class ShareIntent(val share: Intent): PlaylistDetailState
    data object ToExit: PlaylistDetailState
}