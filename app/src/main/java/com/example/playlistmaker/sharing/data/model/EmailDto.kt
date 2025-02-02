package com.example.playlistmaker.sharing.data.model

data class EmailDto(
    val mailBox: Array<String>,
    val subj: String,
    val text: String,
    val data: String,
)
