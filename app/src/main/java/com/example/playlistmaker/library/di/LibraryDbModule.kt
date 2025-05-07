package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.db.PlaylistRepositoryImpl
import com.example.playlistmaker.library.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val libraryDbModule = module {
    single {
        PlaylistDbConverter()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }
}