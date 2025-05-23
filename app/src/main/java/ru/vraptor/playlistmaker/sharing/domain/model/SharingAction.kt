package ru.vraptor.playlistmaker.sharing.domain.model

import android.content.Intent

enum class SharingAction(val value: String) {
    SETTING_SEND(Intent.ACTION_SEND),
    SETTING_SEND_TO(Intent.ACTION_SENDTO),
    SETTING_VIEW(Intent.ACTION_VIEW)
}