package ru.vraptor.playlistmaker.search.data

data class TracksSearchRequest(override val text: String): TracksRequest(text)
