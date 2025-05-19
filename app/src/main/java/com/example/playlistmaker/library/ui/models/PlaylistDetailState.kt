package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track

sealed interface PlaylistDetailState {
    data class Loading(val playlist: Playlist) : PlaylistDetailState
    data class Context(val tracks: List<Track>) : PlaylistDetailState
}