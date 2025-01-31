package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.model.AppSettings

interface SettingsRepository {
    fun getSettings(): AppSettings
    fun updateSettings(settings: AppSettings)

}