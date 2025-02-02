package com.example.playlistmaker.sharing.domain

import android.content.Intent
import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingInteractor {

    fun share(message: String, messageSubj: String): Intent
    fun sendEmail(mailData: EmailData): Intent
    fun sendUrl(url: String): Intent
}