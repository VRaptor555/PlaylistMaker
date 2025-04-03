package com.example.playlistmaker.player.ui.models

sealed class PlayerState(val isPlayButtonEnable: Boolean, val buttonText: String, val progress: String) {
    class Default: PlayerState(false, "PLAY", "--:--")
    class Prepared: PlayerState(true, "PLAY", "00:00")
    class Playing(progress: String): PlayerState(true, "PAUSE", progress)
    class Paused(progress: String): PlayerState(true, "PLAY", progress)
}