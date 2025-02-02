package com.example.playlistmaker.sharing.data.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.dto.ShareRepository
import com.example.playlistmaker.sharing.domain.model.IntentExtra
import com.example.playlistmaker.sharing.domain.model.IntentExtraStr
import com.example.playlistmaker.sharing.domain.model.SharingAction

class ShareRepositoryImpl(
    private val action: SharingAction,
    private val uri: String = ""
): ShareRepository {

    private var intent: Intent = if (action == SharingAction.SETTING_VIEW) {
        Intent(action.value, Uri.parse(uri))
    } else {
        Intent(action.value)
    }


    override fun getIntent(): Intent {
        return intent
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

    override fun setType(typeIntent: String) {
        intent.setType(typeIntent)
    }
}