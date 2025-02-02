package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.main.ui.App
import com.example.playlistmaker.settings.domain.model.AppSettings
import com.example.playlistmaker.settings.ui.models.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory()) [SettingsViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.setSettings(AppSettings(darkTheme = isChecked))
        }

        binding.home.setOnClickListener {
            this.finish()
        }

        viewModel.getSettings()

        binding.shareBtn.setOnClickListener {
            viewModel.sendShare(
                getString(R.string.share_message),
                getString(R.string.share_message_subj)
            )
        }
        binding.supportBtn.setOnClickListener {
            viewModel.sendEmail(
                EmailData(
                    mailBox = arrayOf(getString(R.string.support_message_email)),
                    subj = getString(R.string.support_message_subj),
                    text = getString(R.string.support_message_text),
                    data = "mailto:"
                )
            )
        }
        binding.agreementBtn.setOnClickListener {
            viewModel.sendUrl(getString(R.string.agreement_url))
        }
    }

    private fun settingValuesSet(setting: AppSettings) {
        (applicationContext as App).switchTheme(setting.darkTheme)
    }

    private fun settingValuesGet(setting: AppSettings) {
        binding.themeSwitcher.isChecked = setting.darkTheme
        (applicationContext as App).switchTheme(setting.darkTheme)
    }

    private fun shareAction(share: Intent) {
        startActivity(Intent.createChooser(share, null))
    }

    private fun sendToAction(action: Intent) {
        startActivity(action)
    }

    private fun render(state: SettingsState) {
        when(state) {
            is SettingsState.GetSettings -> settingValuesGet(state.settings)
            is SettingsState.SetSettings -> settingValuesSet(state.settings)
            is SettingsState.EmailIntent -> sendToAction(state.email)
            is SettingsState.ShareIntent -> shareAction(state.share)
            is SettingsState.UrlIntent -> sendToAction(state.url)
        }
    }
}