package ru.vraptor.playlistmaker.settings.di

import ru.vraptor.playlistmaker.settings.domain.SettingsRepository
import ru.vraptor.playlistmaker.settings.data.impl.SettingsStorageRepository
import ru.vraptor.playlistmaker.settings.domain.SettingsInteractor
import ru.vraptor.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> {
        SettingsStorageRepository(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}