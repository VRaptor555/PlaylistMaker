package ru.vraptor.playlistmaker.library.di

import ru.vraptor.playlistmaker.library.db.PlaylistRepositoryImpl
import ru.vraptor.playlistmaker.library.db.TracklistRepositoryImpl
import ru.vraptor.playlistmaker.library.db.converters.PlaylistDbConverter
import ru.vraptor.playlistmaker.library.db.converters.TracklistDbConverter
import ru.vraptor.playlistmaker.library.domain.db.PlaylistInteractor
import ru.vraptor.playlistmaker.library.domain.db.PlaylistRepository
import ru.vraptor.playlistmaker.library.domain.db.TracklistRepository
import ru.vraptor.playlistmaker.library.domain.impl.PlaylistInteractorImpl
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