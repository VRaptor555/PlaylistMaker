package ru.vraptor.playlistmaker.search.di

import ru.vraptor.playlistmaker.search.domain.TracksHistoryInteractor
import ru.vraptor.playlistmaker.search.domain.TracksInteractor
import ru.vraptor.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import ru.vraptor.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchModule = module {
    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(get())
    }
}