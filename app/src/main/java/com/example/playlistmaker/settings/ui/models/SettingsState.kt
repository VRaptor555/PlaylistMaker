package com.example.playlistmaker.settings.ui.models

import android.content.Intent
import com.example.playlistmaker.settings.domain.model.AppSettings

sealed interface SettingsState {
    data class SetSettings(
        val settings: AppSettings
    ): SettingsState

    data class GetSettings(
        val settings: AppSettings
    ): SettingsState

    data class EmailIntent(
        val email: Intent
    ): SettingsState

    data class UrlIntent(
        val url: Intent
    ): SettingsState

    data class ShareIntent(
        val share: Intent
    ): SettingsState
}