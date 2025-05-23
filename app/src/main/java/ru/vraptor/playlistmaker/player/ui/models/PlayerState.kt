package ru.vraptor.playlistmaker.player.ui.models

import ru.vraptor.playlistmaker.search.domain.model.Track

sealed class PlayerState(
    val isPlayButtonEnable: Boolean,
    val buttonText: String,
    val progress: String,
    val isFavorite: Boolean,
    val track: Track?,
) {
    class Default(track: Track): PlayerState(false, "PLAY", "--:--", false, track)
    class Prepared(isFavorite: Boolean): PlayerState(true, "PLAY", "00:00", isFavorite, null)
    class Playing(progress: String, isFavorite: Boolean): PlayerState(true, "PAUSE", progress, isFavorite, null)
    class Paused(progress: String, isFavorite: Boolean): PlayerState(true, "PLAY", progress, isFavorite, null)
}