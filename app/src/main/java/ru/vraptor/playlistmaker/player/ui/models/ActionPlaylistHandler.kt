package ru.vraptor.playlistmaker.player.ui.models

import ru.vraptor.playlistmaker.library.domain.model.Playlist

interface ActionPlaylistHandler {
    fun handlerAction(actionPlaylist: Playlist)
}