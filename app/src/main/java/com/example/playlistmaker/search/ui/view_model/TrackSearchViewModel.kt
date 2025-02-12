package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.TracksConsumer
import com.example.playlistmaker.search.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.models.TracksState

class TrackSearchViewModel(
    application: Application,
    val trackInteractor: TracksInteractor,
    val searchHistoryInteractor: TracksHistoryInteractor,
): AndroidViewModel(application) {
    private val tracks = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    private val mediatorTracksStateLiveData = MediatorLiveData<TracksState>().also { liveData ->
        liveData.addSource(stateLiveData) { trackState ->
            liveData.value = when(trackState) {
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

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun startSearch(searchTracks: String) {
        if (searchTracks.isNotEmpty()) {
            renderState(TracksState.Loading)

            trackInteractor.searchTracks(
                searchTracks,
                object : TracksConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        if (foundTracks != null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                        }
                        when {
                            errorMessage != null -> {
                                renderState(TracksState.Error(
                                    errorMessage = getApplication<Application>().getString(
                                    R.string.something_went_wrong)))
                            }
                            tracks.isEmpty() -> {
                                renderState(TracksState.Empty(
                                    message = getApplication<Application>().getString(
                                        R.string.nothing_found
                                    )
                                ))
                            }
                            else -> renderState(
                                TracksState.Content(
                                    tracks = tracks
                                )
                            )
                        }
                    }

                })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    fun searchDebounce(searchText: String) {
        if (latestSearchText == searchText) {
            return
        }
        this.latestSearchText = searchText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { startSearch(searchText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun restartSearch() {
        if (latestSearchText == null || latestSearchText?.length == 0) {
            return
        }
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { startSearch(latestSearchText!!) }
        val postTime = SystemClock.uptimeMillis()
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addToSavedTracksList(track)
    }

    fun getHistoryTrackList() {
        renderState(TracksState.Loading)
        tracks.clear()
        tracks.addAll(searchHistoryInteractor.getSavedTracksList())
        if (tracks.isEmpty()) {
            renderState(TracksState.EmptyHistory)
        } else {
            renderState(TracksState.ContentHistory(tracks = tracks))
        }
    }

    fun clearHistoryList() {
        tracks.clear()
        searchHistoryInteractor.clearTracks()
        renderState(TracksState.EmptyHistory)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}