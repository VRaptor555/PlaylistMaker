package ru.vraptor.playlistmaker.search.domain.impl

import ru.vraptor.playlistmaker.search.domain.TracksInteractor
import ru.vraptor.playlistmaker.search.domain.TracksRepository
import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(text: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(text).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}