package com.example.playlistmaker.presentation

import android.content.Intent
import com.example.playlistmaker.domain.impl.SettingsIntentImpl
import com.example.playlistmaker.domain.models.IntentExtra
import com.example.playlistmaker.domain.models.IntentExtraStr
import com.example.playlistmaker.domain.models.SettingAction

class SettingsActionImpl {

    fun share(message: String, messageSubj: String): Intent {
        val settingSend = SettingsIntentImpl(SettingAction.SETTING_SEND)
        settingSend.setType("text/plain")
        val listIntentValue = listOf(
            IntentExtraStr(Intent.EXTRA_TEXT, message),
            IntentExtraStr(Intent.EXTRA_SUBJECT, messageSubj)
            )
        settingSend.putExtra(listIntentValue)
        return settingSend.intent
    }

    fun sendEmail(mailbox: Array<String>, subj: String, text: String, data: String): Intent {
        val sending = SettingsIntentImpl(SettingAction.SETTING_SEND_TO)
        sending.setData(data)
        val listIntentValue = listOf(
            IntentExtraStr(Intent.EXTRA_SUBJECT, subj),
            IntentExtraStr(Intent.EXTRA_TEXT, text),
            IntentExtra(Intent.EXTRA_EMAIL, mailbox)
        )
        sending.putExtra(listIntentValue)
        return sending.intent
    }

    fun sendUrl(url: String): Intent {
        return SettingsIntentImpl(SettingAction.SETTING_VIEW, url).intent
    }
}