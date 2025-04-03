package com.example.playlistmaker.sharing.data.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.domain.ShareRepository
import com.example.playlistmaker.sharing.domain.model.IntentExtra
import com.example.playlistmaker.sharing.domain.model.IntentExtraStr
import com.example.playlistmaker.sharing.domain.model.SharingAction
import androidx.core.net.toUri

class ShareRepositoryImpl(
    action: SharingAction,
    uri: String = ""
): ShareRepository {

    private var intent: Intent = if (action == SharingAction.SETTING_VIEW) {
        Intent(action.value, uri.toUri())
    } else {
        Intent(action.value)
    }


    override fun getIntent(): Intent {
        return intent
    }

    override fun putExtra(extra: List<Any>) {
        for (extraValue in extra) {
            when (extraValue) {
                is IntentExtra -> intent.putExtra(extraValue.typeIntent, extraValue.valueIntent)
                is IntentExtraStr -> intent.putExtra(extraValue.typeIntent, extraValue.valueIntent)
            }
        }
    }

    override fun setData(data: String) {
        intent.data = data.toUri()
    }

    override fun setType(typeIntent: String) {
        intent.setType(typeIntent)
    }
}