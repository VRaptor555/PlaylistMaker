package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.models.TracksState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    application: Application,
    private val trackInteractor: TracksInteractor,
    private val searchHistoryInteractor: TracksHistoryInteractor,
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
        private const val RESEARCH_DEBOUNCE_DELAY = 500L
    }

}