package ru.vraptor.playlistmaker.settings.data

import ru.vraptor.playlistmaker.settings.data.model.AppSettingsDto
import ru.vraptor.playlistmaker.settings.domain.model.AppSettings

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