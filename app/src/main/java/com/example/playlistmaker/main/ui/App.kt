package com.example.playlistmaker.main.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class App: Application() {
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