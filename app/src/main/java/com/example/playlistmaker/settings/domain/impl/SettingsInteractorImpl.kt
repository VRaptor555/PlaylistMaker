package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.AppSettings

class SettingsInteractorImpl(
    private val sharedSettings: SettingsRepository
): SettingsInteractor {
    override fun getSettings(): AppSettings {
        return sharedSettings.getSettings()
    }

    override fun updateSettings(settings: AppSettings) {
        sharedSettings.updateSettings(settings)
    }
}