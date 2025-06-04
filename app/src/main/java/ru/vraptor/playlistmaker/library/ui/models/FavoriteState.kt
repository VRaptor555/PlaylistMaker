package ru.vraptor.playlistmaker.library.ui.models

import ru.vraptor.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {
    data object Empty: FavoriteState
    data class Content(
        val tracks: List<Track>
    ): FavoriteState
}