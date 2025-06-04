package ru.vraptor.playlistmaker.search.ui.models

import ru.vraptor.playlistmaker.search.domain.model.Track

sealed interface TracksState {
    object Loading: TracksState

    data class Content(
        val tracks: List<Track>
    ): TracksState

    data class ContentHistory(
        val tracks: List<Track>
    ): TracksState

    data class Error(
        val errorMessage: String
    ): TracksState

    data class Empty(
        val message: String
    ): TracksState

    object EmptyHistory: TracksState
}