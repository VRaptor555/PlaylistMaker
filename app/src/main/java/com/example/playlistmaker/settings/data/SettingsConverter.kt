package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.data.model.AppSettingsDto
import com.example.playlistmaker.settings.domain.model.AppSettings

object SettingsConverter {
    fun dtoToSettings(dtoSettings: AppSettingsDto): AppSettings {
        return AppSettings(
            darkTheme = dtoSettings.darkTheme
        )
    }

    fun settingsToDto(settings: AppSettings): AppSettingsDto {
        return AppSettingsDto(
            darkTheme = settings.darkTheme
        )
    }
}