package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.dsl.module

val playerModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

}