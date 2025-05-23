package ru.vraptor.playlistmaker.sharing.di

import ru.vraptor.playlistmaker.sharing.domain.SharingInteractor
import ru.vraptor.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl()
    }
}