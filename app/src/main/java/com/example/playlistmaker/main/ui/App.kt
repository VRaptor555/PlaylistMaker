package com.example.playlistmaker.main.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.AppSettings


class App: Application() {
    var darkTheme = false
    private lateinit var settingsApp: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsApp = Creator.provideSettings(this)
        switchTheme(settingsApp.getSettings().darkTheme)
    }

    private fun saveConfiguration(){
        settingsApp.updateSettings(AppSettings(darkTheme = darkTheme))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveConfiguration()
    }
}