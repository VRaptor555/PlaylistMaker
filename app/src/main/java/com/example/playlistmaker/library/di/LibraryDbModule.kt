package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.db.PlaylistRepositoryImpl
import com.example.playlistmaker.library.db.TracklistRepositoryImpl
import com.example.playlistmaker.library.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.db.converters.TracklistDbConverter
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.db.TracklistRepository
import com.example.playlistmaker.library.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val libraryDbModule = module {
    single {
        PlaylistDbConverter()
    }

    single {
        TracklistDbConverter()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }

    single<TracklistRepository> {
        TracklistRepositoryImpl(get(), get())
    }
}