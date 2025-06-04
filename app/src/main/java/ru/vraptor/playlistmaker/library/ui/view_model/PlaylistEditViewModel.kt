package ru.vraptor.playlistmaker.library.ui.view_model

import ru.vraptor.playlistmaker.library.domain.db.PlaylistInteractor
import ru.vraptor.playlistmaker.library.domain.model.Playlist

class PlaylistEditViewModel(
    override var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor
    ): PlaylistCreateViewModel(playlist, playlistInteractor) {

    override suspend fun savePlaylistToDb(): Boolean {
        playlistInteractor.updatePlaylist(playlist)
        return true
    }
}