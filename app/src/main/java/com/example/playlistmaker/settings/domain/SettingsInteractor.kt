package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.domain.model.AppSettings

interface SettingsInteractor {
    fun getSettings(): AppSettings
    fun setSettings(settings: AppSettings)
}