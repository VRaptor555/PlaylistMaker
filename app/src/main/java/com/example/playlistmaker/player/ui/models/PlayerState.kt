package com.example.playlistmaker.player.ui.models

sealed class PlayerState(
    val isPlayButtonEnable: Boolean,
    val buttonText: String,
    val progress: String,
    val isFavorite: Boolean
) {
    class Default: PlayerState(false, "PLAY", "--:--", false)
    class Prepared(isFavorite: Boolean): PlayerState(true, "PLAY", "00:00", isFavorite)
    class Playing(progress: String, isFavorite: Boolean): PlayerState(true, "PAUSE", progress, isFavorite)
    class Paused(progress: String, isFavorite: Boolean): PlayerState(true, "PLAY", progress, isFavorite)
}