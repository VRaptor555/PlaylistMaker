package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.utils.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(text: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(text)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }

        }
    }
}