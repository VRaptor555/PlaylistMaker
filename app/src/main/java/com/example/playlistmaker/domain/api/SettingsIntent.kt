package com.example.playlistmaker.domain.api

interface SettingsIntent {
    fun setType(typeIntent: String)
    fun putExtra(extra: List<Any>)
    fun setData(data: String)
    fun getIntent(): Any
}