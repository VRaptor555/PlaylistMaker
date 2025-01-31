package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.dto.SettingsStorage
import com.example.playlistmaker.settings.domain.model.AppSettings

class SettingsRepositoryImpl(
    private val localStorage: SettingsStorage,
): SettingsRepository {
    override fun getSettings(): AppSettings {
        return localStorage.getSettings()
    }

    override fun updateSettings(settings: AppSettings) {
        localStorage.setSettings(settings)
    }
}