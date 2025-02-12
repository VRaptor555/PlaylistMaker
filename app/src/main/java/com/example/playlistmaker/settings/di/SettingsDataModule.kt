package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.dto.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsStorageRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> {
        SettingsStorageRepository(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}