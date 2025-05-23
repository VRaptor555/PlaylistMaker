package ru.vraptor.playlistmaker.main.ui

import android.text.Editable
import android.text.TextWatcher

open class CustomTextWatcher: TextWatcher {
    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}