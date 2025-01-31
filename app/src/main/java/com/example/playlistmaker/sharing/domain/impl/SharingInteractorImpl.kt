package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.settings.domain.model.EmailData
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
): SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        TODO("Not yet implemented")
    }

    private fun getTermsLink(): String {
        TODO("Not yet implemented")
    }

    private fun getSupportEmailData(): EmailData {
        TODO("Not yet implemented")
    }
}