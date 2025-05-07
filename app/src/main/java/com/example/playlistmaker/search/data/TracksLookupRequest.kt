package com.example.playlistmaker.search.data

data class TracksLookupRequest(override val text: String): TracksRequest(text)
