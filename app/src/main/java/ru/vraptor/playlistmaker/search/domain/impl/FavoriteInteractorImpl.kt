package ru.vraptor.playlistmaker.search.domain.impl

import ru.vraptor.playlistmaker.search.domain.db.FavoriteInteractor
import ru.vraptor.playlistmaker.search.domain.db.FavoriteRepository
import ru.vraptor.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository
): FavoriteInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun delTrack(track: Track) {
        repository.delTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }
}