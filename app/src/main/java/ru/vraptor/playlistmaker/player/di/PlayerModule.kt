package ru.vraptor.playlistmaker.player.di

import ru.vraptor.playlistmaker.player.domain.PlayerInteractor
import ru.vraptor.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.dsl.module

val playerModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

}