package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.HistoryRepository
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksApiService
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.utils.API_URL
import com.example.playlistmaker.utils.PLAYLIST_MAKER_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    single<TracksApiService> {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TracksApiService::class.java)
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
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

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

}