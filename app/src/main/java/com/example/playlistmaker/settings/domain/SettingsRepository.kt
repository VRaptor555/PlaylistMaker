package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.data.model.AppSettingsDto

interface SettingsRepository {
    fun getSettings(): AppSettingsDto
    fun setSettings(settings: AppSettingsDto)
}