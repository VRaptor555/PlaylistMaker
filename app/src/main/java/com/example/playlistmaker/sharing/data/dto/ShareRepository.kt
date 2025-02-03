package com.example.playlistmaker.sharing.data.dto

import android.content.Intent

interface ShareRepository {
    fun setType(typeIntent: String)
    fun putExtra(extra: List<Any>)
    fun setData(data: String)
    fun getIntent(): Intent
}