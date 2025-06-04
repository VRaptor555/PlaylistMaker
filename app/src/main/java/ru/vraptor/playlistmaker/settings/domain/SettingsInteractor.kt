package ru.vraptor.playlistmaker.settings.domain

import ru.vraptor.playlistmaker.settings.domain.model.AppSettings

interface SettingsInteractor {
    fun getSettings(): AppSettings
    fun setSettings(settings: AppSettings)
}