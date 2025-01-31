package com.example.playlistmaker.settings.data.dto

import android.app.Application
import android.app.Application.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.main.ui.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.settings.domain.model.AppSettings
import com.google.gson.Gson

class SettingsStorage(
    private val application: Application
) {
    private lateinit var sharedPreferences: SharedPreferences
    init {
        sharedPreferences = application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
    }

    private companion object{
        const val SETTINGS_KEY = "SETTINGS_KEY"
    }

    private fun defaultSettings(): AppSettings {
        val DarkModeFlags = application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = DarkModeFlags == Configuration.UI_MODE_NIGHT_YES

        return AppSettings(
            darkTheme = isDarkModeOn
        )
    }

    fun getSettings(): AppSettings {
        val json = sharedPreferences.getString(SETTINGS_KEY, null) ?: return defaultSettings()
        return Gson().fromJson(json, AppSettings::class.java)
    }

    fun setSettings(settings: AppSettings) {
        val json = Gson().toJson(settings)
        sharedPreferences.edit()
            .putString(SETTINGS_KEY, json)
            .apply()
    }

}