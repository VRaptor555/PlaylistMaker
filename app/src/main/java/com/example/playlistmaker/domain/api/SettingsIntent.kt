package com.example.playlistmaker.domain.api

import android.content.Intent

interface SettingsIntent {
    var intent: Intent
    fun setType(typeIntent: String)
    fun putExtra(extra: List<Any>)
    fun setData(data: String)
}