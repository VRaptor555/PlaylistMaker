package com.example.playlistmaker.library.db

import com.example.playlistmaker.library.db.converters.TracklistDbConverter
import com.example.playlistmaker.library.db.entity.TracklistEntity
import com.example.playlistmaker.library.domain.db.TracklistRepository
import com.example.playlistmaker.search.data.db.AppDatabase
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracklistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TracklistDbConverter
): TracklistRepository {
    override suspend fun addTrack(track: Track) {
        val tracklist = converter.map(track)
        appDatabase.tracklistDao().insertTrack(tracklist)
    }

    override suspend fun deleteTrack(track: Track) {
        val tracklist = converter.map(track)
        appDatabase.tracklistDao().deleteTrack(tracklist)
    }

    override fun getTrackById(idTrack: Long): Flow<Track?> = flow {
        val track = appDatabase.tracklistDao().getTrack(idTrack)
        if (track == null) {
            emit(null)
        } else {
            emit(converter.map(track))
        }
    }

    override fun getTrackListById(idList: List<Long>): Flow<List<Track>> = flow {
        val tracks = mutableListOf<Track>()
        for(idTrack in idList) {
            getTrackById(idTrack).collect { track ->
                track?.let {
                    tracks.add(0, it) }
                }
        }
        emit(tracks)
    }
}