package ru.vraptor.playlistmaker.search.di

import android.content.Context
import ru.vraptor.playlistmaker.search.data.dto.TracksListHistoryStorage
import ru.vraptor.playlistmaker.search.data.impl.HistoryRepositoryImpl
import ru.vraptor.playlistmaker.search.data.impl.TracksRepositoryImpl
import ru.vraptor.playlistmaker.search.data.network.NetworkClient
import ru.vraptor.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.vraptor.playlistmaker.search.domain.HistoryRepository
import ru.vraptor.playlistmaker.search.domain.TracksRepository
import ru.vraptor.playlistmaker.utils.API_URL
import ru.vraptor.playlistmaker.utils.PLAYLIST_MAKER_PREFERENCES
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vraptor.playlistmaker.search.data.network.ITunesApiService

val searchDataModule = module {
    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single<TracksListHistoryStorage> {
        TracksListHistoryStorage(get(), get())
    }

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }
}