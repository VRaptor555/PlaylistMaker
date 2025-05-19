package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.db.TracklistRepository
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class PlaylistInteractorImpl(
    private val repositoryPlaylist: PlaylistRepository,
    private val repositoryTracklist: TracklistRepository
): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist): Long {
        return repositoryPlaylist.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repositoryPlaylist.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repositoryPlaylist.getPlaylists()
    }

    override fun getTracksFromPlaylist(tracksId: List<Long>): Flow<List<Track>> {
        return repositoryTracklist.getTrackListById(tracksId)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        val currentListTracks = playlist.tracksId
        if (currentListTracks.contains(track.trackId)) {
            return false
        }
        val trackInBase = repositoryTracklist.getTrackById(track.trackId).single()
        if (trackInBase == null) {
            repositoryTracklist.addTrack(track)
        }
        val tracksIdModified = currentListTracks + track.trackId
        playlist.tracksId = tracksIdModified
        playlist.countTracks = playlist.tracksId.size
        repositoryPlaylist.updatePlaylist(playlist)
        return true
    }

    override suspend fun deleteTrackFromPlaylist(
        playlist: Playlist,
        track: Track
    ): Playlist {
        val currentListTracks = playlist.tracksId
        val tracksIdModifies = currentListTracks.filter { it != track.trackId }
        playlist.tracksId = tracksIdModifies
        playlist.countTracks = playlist.tracksId.size
        repositoryPlaylist.updatePlaylist(playlist)
        repositoryPlaylist.getPlaylists().collect { playlists ->
            deleteTrackFromBase(playlists, track)
        }
        return playlist
    }

    private suspend fun deleteTrackFromBase(playlists: List<Playlist>, track: Track) {
        var isFound = false
        for (playlist in playlists) {
            if (track.trackId in playlist.tracksId) {
                isFound = true
                break
            }
        }
        if (!isFound) {
            repositoryTracklist.deleteTrack(track)
        }
    }
}