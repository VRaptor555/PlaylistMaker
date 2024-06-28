package com.example.playlistmaker.api

import com.example.playlistmaker.tracks.Track

class TrackResponse(
    val resultCount: Int,
    val results: List<Track>) {
}