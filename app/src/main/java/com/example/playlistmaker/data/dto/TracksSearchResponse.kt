package com.example.playlistmaker.data.dto

class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>): Response()