package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchModule = module {
    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(get())
    }
}