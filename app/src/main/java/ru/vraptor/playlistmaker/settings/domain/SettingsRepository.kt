package ru.vraptor.playlistmaker.settings.domain

import ru.vraptor.playlistmaker.settings.data.model.AppSettingsDto

interface SettingsRepository {
    fun getSettings(): AppSettingsDto
    fun setSettings(settings: AppSettingsDto)
}