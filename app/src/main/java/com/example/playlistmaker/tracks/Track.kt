package com.example.playlistmaker.tracks

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
)

interface TrackObserver {
    fun goneTrack(track: Track)
}

interface TrackObservable {
    fun add(observer: TrackObserver)
    fun remove(observer: TrackObserver)
    fun notifyObservers(track: Track)
}
