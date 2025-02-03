package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.data.SettingsConverter.dtoToSettings
import com.example.playlistmaker.settings.data.SettingsConverter.settingsToDto
import com.example.playlistmaker.settings.data.dto.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.AppSettings

class SettingsInteractorImpl(
    private val settings: SettingsRepository
): SettingsInteractor {
    override fun getSettings(): AppSettings {
        return dtoToSettings(settings.getSettings())
    }

    override fun setSettings(settings: AppSettings) {
        this.settings.setSettings(settingsToDto(settings))
    }
}