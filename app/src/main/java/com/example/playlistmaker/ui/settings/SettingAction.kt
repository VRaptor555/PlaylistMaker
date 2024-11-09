package com.example.playlistmaker.ui.settings

import android.content.Intent

enum class SettingAction(val value: String) {
    SETTING_SEND(Intent.ACTION_SEND),
    SETTING_SEND_TO(Intent.ACTION_SENDTO),
    SETTING_VIEW(Intent.ACTION_VIEW)
}