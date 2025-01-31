package com.example.playlistmaker.settings.domain

interface SettingsIntent {
    fun setType(typeIntent: String)
    fun putExtra(extra: List<Any>)
    fun setData(data: String)
    fun getIntent(): Any
}