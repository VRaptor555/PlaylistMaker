package ru.vraptor.playlistmaker.library.ui.models

import android.content.Intent
import ru.vraptor.playlistmaker.library.domain.model.Playlist
import ru.vraptor.playlistmaker.search.domain.model.Track

sealed interface PlaylistDetailState {
    data object Loading: PlaylistDetailState
    data class Content(val playlist: Playlist, val tracks: List<Track>): PlaylistDetailState
    data class SendMessage(val message: String): PlaylistDetailState
    data class ShareIntent(val share: Intent): PlaylistDetailState
    data class ShowPlaylist(val playlist: Playlist): PlaylistDetailState
    data object ToExit: PlaylistDetailState
}