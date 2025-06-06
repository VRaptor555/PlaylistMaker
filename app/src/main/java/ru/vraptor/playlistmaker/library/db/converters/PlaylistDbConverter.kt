package ru.vraptor.playlistmaker.library.db.converters

import ru.vraptor.playlistmaker.library.db.entity.PlaylistEntity
import ru.vraptor.playlistmaker.library.domain.model.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            playlist.tracksId,
            playlist.countTracks
        )
    }

    fun map(playlistTable: PlaylistEntity): Playlist {
        return Playlist(
            playlistTable.id,
            playlistTable.namePlaylist,
            playlistTable.description,
            playlistTable.imagePath,
            playlistTable.tracksId,
            playlistTable.countTracks
        )
    }
}