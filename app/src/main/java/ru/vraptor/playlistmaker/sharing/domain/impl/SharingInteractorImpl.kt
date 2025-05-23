package ru.vraptor.playlistmaker.sharing.domain.impl

import android.content.Intent
import ru.vraptor.playlistmaker.sharing.data.impl.ShareRepositoryImpl
import ru.vraptor.playlistmaker.sharing.domain.SharingInteractor
import ru.vraptor.playlistmaker.sharing.domain.model.EmailData
import ru.vraptor.playlistmaker.sharing.domain.model.IntentExtra
import ru.vraptor.playlistmaker.sharing.domain.model.IntentExtraStr
import ru.vraptor.playlistmaker.sharing.domain.model.SharingAction

class SharingInteractorImpl(): SharingInteractor {

    override fun sendEmail(mailData: EmailData): Intent {
        val share = ShareRepositoryImpl(SharingAction.SETTING_SEND_TO)
        val listIntentValue = listOf(
            IntentExtraStr(Intent.EXTRA_SUBJECT, mailData.subj),
            IntentExtraStr(Intent.EXTRA_TEXT, mailData.text),
            IntentExtra(Intent.EXTRA_EMAIL, mailData.mailBox),
        )
        share.putExtra(listIntentValue)
        share.setData(mailData.data)
        return share.getIntent()
    }

    override fun sendUrl(url: String): Intent {
        return ShareRepositoryImpl(SharingAction.SETTING_VIEW, url).getIntent()
    }

    override fun share(message: String, messageSubj: String): Intent {
        val share = ShareRepositoryImpl(SharingAction.SETTING_SEND)
        share.setType("text/plain")
        val listIntentValue = listOf(
            IntentExtraStr(Intent.EXTRA_TEXT, message),
            IntentExtraStr(Intent.EXTRA_SUBJECT, messageSubj),
        )
        share.putExtra(listIntentValue)
        return share.getIntent()
    }
}