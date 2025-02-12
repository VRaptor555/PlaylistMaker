package com.example.playlistmaker.settings.data.impl

import android.app.Application
import android.app.Application.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.playlistmaker.settings.data.dto.SettingsRepository
import com.example.playlistmaker.settings.data.model.AppSettingsDto
import com.example.playlistmaker.utils.PLAYLIST_MAKER_PREFERENCES
import com.google.gson.Gson

class SettingsStorageRepository(
    private val application: Application,
): SettingsRepository {


    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

    private companion object{
        const val SETTINGS_KEY = "SETTINGS_KEY"
    }

    private fun defaultSettings(): AppSettingsDto {
        val darkModeFlags = application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkModeOn = darkModeFlags == Configuration.UI_MODE_NIGHT_YES

        return AppSettingsDto(
            darkTheme = isDarkModeOn
        )
    }

    override fun getSettings(): AppSettingsDto {
        val json = sharedPreferences.getString(SETTINGS_KEY, null) ?: return defaultSettings()
        return Gson().fromJson(json, AppSettingsDto::class.java)
    }

    override fun setSettings(settings: AppSettingsDto) {
        val json = Gson().toJson(settings)
        sharedPreferences.edit()
            .putString(SETTINGS_KEY, json)
            .apply()
    }


}