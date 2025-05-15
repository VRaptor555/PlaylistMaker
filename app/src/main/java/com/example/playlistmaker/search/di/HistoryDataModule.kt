package com.example.playlistmaker.search.di

import androidx.room.Room
import com.example.playlistmaker.search.domain.db.FavoriteRepository
import com.example.playlistmaker.search.data.db.FavoriteRepositoryImpl
import com.example.playlistmaker.search.data.converters.TrackDbConverter
import com.example.playlistmaker.search.data.db.AppDatabase
import com.example.playlistmaker.search.domain.db.FavoriteInteractor
import com.example.playlistmaker.search.domain.impl.FavoriteInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val historyDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single {
        TrackDbConverter()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}