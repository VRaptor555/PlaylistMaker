package com.example.playlistmaker.player.ui.models

sealed interface PlayerState {
    object Loading: PlayerState

    data class IsPlay(
        val timeCode: String
    ): PlayerState

    data class IsPause(
        val timeCode: String
    ): PlayerState
}