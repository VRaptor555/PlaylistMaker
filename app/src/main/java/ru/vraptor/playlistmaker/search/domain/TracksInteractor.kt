package ru.vraptor.playlistmaker.search.domain

import ru.vraptor.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(text: String): Flow<Pair<List<Track>?, String?>>
}