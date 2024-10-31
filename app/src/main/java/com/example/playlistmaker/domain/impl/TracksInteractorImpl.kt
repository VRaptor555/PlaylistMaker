package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.data.dto.TracksSearchResponse
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor  {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(text))
        }
    }
}