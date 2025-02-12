package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.HistoryRepository
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TracksRepository
import org.koin.dsl.module

val searchDataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    single<TracksListHistoryStorage> {
        TracksListHistoryStorage(get())
    }

}