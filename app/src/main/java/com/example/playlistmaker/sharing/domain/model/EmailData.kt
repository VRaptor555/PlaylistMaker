package com.example.playlistmaker.sharing.domain.model

data class EmailData(
    val mailBox: Array<String>,
    val subj: String,
    val text: String,
    val data: String,
)