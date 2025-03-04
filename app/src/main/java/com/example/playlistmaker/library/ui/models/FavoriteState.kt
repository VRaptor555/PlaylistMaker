package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {
    data object Loading: FavoriteState
    data object Empty: FavoriteState

    data class Content(
        val tracks: List<Track>
    ): FavoriteState

    data class Error(
        val errorMessage: String
    ): FavoriteState
}