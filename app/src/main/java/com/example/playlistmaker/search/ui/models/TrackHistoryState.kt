package com.example.playlistmaker.search.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface TracksHistoryState {
    object Loading: TracksHistoryState
    object Empty: TracksHistoryState

    data class Content(
        val tracks: List<Track>
    ): TracksHistoryState

}