package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val sharingModule = module {
    single<SharingInteractor> {
        SharingInteractorImpl()
    }
}