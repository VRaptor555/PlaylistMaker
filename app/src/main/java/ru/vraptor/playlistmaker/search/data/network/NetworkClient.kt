package ru.vraptor.playlistmaker.search.data.network

import ru.vraptor.playlistmaker.search.data.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}