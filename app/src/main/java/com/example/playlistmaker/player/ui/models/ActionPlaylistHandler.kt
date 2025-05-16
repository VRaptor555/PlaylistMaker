package com.example.playlistmaker.player.ui.models

import com.example.playlistmaker.library.domain.model.Playlist

interface ActionPlaylistHandler {
    fun handlerAction(actionPlaylist: Playlist)
}