package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    private val repositoryPlaylist: PlaylistRepository,
    private val repositoryTracks: TracksRepository
): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repositoryPlaylist.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repositoryPlaylist.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repositoryPlaylist.getPlaylists()
    }

    override fun getTracksFromPlaylist(tracksId: String): Flow<Pair<List<Track>?, String?>> {
        return repositoryTracks.getTracksFromId(tracksId).map { result ->
            when(result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }

    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val currentListTracks = playlist.tracksId
        if (currentListTracks.contains(track.trackId)) {
            return
        }
        val tracksIdModified = currentListTracks + track.trackId
        playlist.tracksId = tracksIdModified
        playlist.countTracks = playlist.tracksId.size
        repositoryPlaylist.updatePlaylist(playlist)
    }


}