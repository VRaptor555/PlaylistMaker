package com.example.playlistmaker.domain.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.SettingsIntent
import com.example.playlistmaker.domain.models.IntentExtra
import com.example.playlistmaker.domain.models.IntentExtraStr
import com.example.playlistmaker.domain.models.SettingAction

class SettingsIntentImpl(action: SettingAction, uri: String = ""): SettingsIntent {
    override var intent: Intent = if (action == SettingAction.SETTING_VIEW) {
        Intent(action.value, Uri.parse(uri))
    } else {
        Intent(action.value)
    }

    override fun setType(typeIntent: String) {
        intent.setType(typeIntent)
    }

    override fun putExtra(extra: List<Any>) {
        for (extraValue in extra) {
            if (extraValue is IntentExtraStr) {
                for (valueIntent in extraValue.valueIntent) {
                    intent.putExtra(extraValue.typeIntent, valueIntent)
                }
            } else if (extraValue is IntentExtra) {
                intent.putExtra(extraValue.typeIntent, extraValue.valueIntent)
            }
        }
    }

    override fun setData(data: String) {
        intent.data = Uri.parse(data)
    }

}