package com.example.playlistmaker.main.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.di.playerModules
import com.example.playlistmaker.search.di.searchModules
import com.example.playlistmaker.settings.data.impl.SettingsStorageRepository
import com.example.playlistmaker.settings.di.settingsModules
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                settingsModules,
                searchModules,
                playerModules,
            )
        }
        val settings = SettingsInteractorImpl(SettingsStorageRepository(this)).getSettings()
        switchTheme(settings.darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}