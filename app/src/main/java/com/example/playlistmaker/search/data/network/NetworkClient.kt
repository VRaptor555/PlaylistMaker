package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}