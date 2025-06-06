package ru.vraptor.playlistmaker.search.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ru.vraptor.playlistmaker.R
import ru.vraptor.playlistmaker.search.domain.TracksHistoryInteractor
import ru.vraptor.playlistmaker.search.domain.TracksInteractor
import ru.vraptor.playlistmaker.search.domain.db.FavoriteInteractor
import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.search.ui.models.TracksState
import ru.vraptor.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    application: Application,
    private val trackInteractor: TracksInteractor,
    private val searchHistoryInteractor: TracksHistoryInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) : AndroidViewModel(application) {
    private val tracks = ArrayList<Track>()

    private val onTrackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { searchString -> startSearch(searchString) }

    private val onTrackResearchDebounce = debounce<String>(
        RESEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { researchString -> startSearch(researchString) }

    private val stateLiveData = MutableLiveData<TracksState>()
    private val mediatorTracksStateLiveData = MediatorLiveData<TracksState>().also { liveData ->
        liveData.addSource(stateLiveData) { trackState ->
            liveData.value = when (trackState) {
                is TracksState.Content -> TracksState.Content(trackState.tracks)
                is TracksState.Empty -> trackState
                is TracksState.Error -> trackState
                is TracksState.Loading -> trackState
                is TracksState.ContentHistory -> TracksState.ContentHistory(trackState.tracks)
                is TracksState.EmptyHistory -> trackState
            }
        }
    }

    fun observeState(): LiveData<TracksState> = mediatorTracksStateLiveData

    private var latestSearchText: String? = null

    private fun startSearch(searchTracks: String) {
        renderState(TracksState.Loading)

        viewModelScope.launch {
            trackInteractor
                .searchTracks(searchTracks)
                .collect { pair -> processResult(pair.first, pair.second) }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        if (foundTracks != null) {
            tracks.clear()
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage != null -> {
                renderState(
                    TracksState.Error(
                        errorMessage = getApplication<Application>().getString(
                            R.string.something_went_wrong
                        )
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    TracksState.Empty(
                        message = getApplication<Application>().getString(
                            R.string.nothing_found
                        )
                    )
                )
            }

            else -> renderState(
                TracksState.Content(
                    tracks = tracks
                )
            )
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun searchTracks(searchTracks: String) {
        if (searchTracks.isNotEmpty() && latestSearchText != searchTracks) {
            latestSearchText = searchTracks
            onTrackSearchDebounce(searchTracks)
        }
    }

    fun restartSearch() {
        if (latestSearchText == null || latestSearchText?.length == 0) {
            return
        }
        onTrackResearchDebounce(latestSearchText!!)
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addToSavedTracksList(track)
    }

    fun getHistoryTrackList() {
        renderState(TracksState.Loading)
        tracks.clear()
        viewModelScope.launch {
            tracks.addAll(searchHistoryInteractor.getSavedTracksList())
            if (tracks.isEmpty()) {
                renderState(TracksState.EmptyHistory)
            } else {
                renderState(TracksState.ContentHistory(tracks))
            }
        }
    }

    fun clearHistoryList() {
        tracks.clear()
        searchHistoryInteractor.clearTracks()
        renderState(TracksState.EmptyHistory)
    }

    fun refreshFavorite(history: Boolean) {
        viewModelScope.launch {
            favoriteInteractor.getTracks()
                .collect { tracks -> reloadFavoriteList(tracks, history) }
        }
    }

    private fun reloadFavoriteList(tracksFavorite: List<Track>, history: Boolean) {
        val indexesOfTrack = tracksFavorite.map {
            it.trackId
        }
        val tracksTmp = tracks.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl,
                isFavorite = it.trackId in indexesOfTrack
            )
        }
        tracks.clear()
        tracks.addAll(tracksTmp)
        if (tracks.isEmpty()) {
            if (history) {
                renderState(TracksState.EmptyHistory)
            } else {
                renderState(TracksState.Empty(message = getApplication<Application>().getString(
                    R.string.nothing_found
                )))
            }
        } else {
            if (history) {
                renderState(TracksState.ContentHistory(tracks))
            } else {
                renderState(TracksState.Content(tracks))
            }

        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
        private const val RESEARCH_DEBOUNCE_DELAY = 500L
    }

}