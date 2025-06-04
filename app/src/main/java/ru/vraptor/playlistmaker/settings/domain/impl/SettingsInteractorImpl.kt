package ru.vraptor.playlistmaker.settings.domain.impl

import ru.vraptor.playlistmaker.settings.data.SettingsConverter.dtoToSettings
import ru.vraptor.playlistmaker.settings.data.SettingsConverter.settingsToDto
import ru.vraptor.playlistmaker.settings.domain.SettingsRepository
import ru.vraptor.playlistmaker.settings.domain.SettingsInteractor
import ru.vraptor.playlistmaker.settings.domain.model.AppSettings

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