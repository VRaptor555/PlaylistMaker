package ru.vraptor.playlistmaker.search.domain

import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(text: String): Flow<Resource<List<Track>>>
}