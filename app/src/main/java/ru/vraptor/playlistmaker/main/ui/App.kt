package ru.vraptor.playlistmaker.main.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.vraptor.playlistmaker.library.di.libraryModules
import ru.vraptor.playlistmaker.player.di.playerModules
import ru.vraptor.playlistmaker.search.di.historyDataModule
import ru.vraptor.playlistmaker.search.di.searchModules
import ru.vraptor.playlistmaker.settings.data.impl.SettingsStorageRepository
import ru.vraptor.playlistmaker.settings.di.settingsModules
import ru.vraptor.playlistmaker.settings.domain.impl.SettingsInteractorImpl
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
                libraryModules,
                historyDataModule,
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