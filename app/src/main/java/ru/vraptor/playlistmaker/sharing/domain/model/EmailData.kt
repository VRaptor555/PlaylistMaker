package ru.vraptor.playlistmaker.sharing.domain.model

data class EmailData(
    val mailBox: List<String>,
    val subj: String,
    val text: String,
    val data: String,
)