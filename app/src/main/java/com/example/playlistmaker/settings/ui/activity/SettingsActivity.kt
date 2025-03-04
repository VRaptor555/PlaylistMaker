package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.main.ui.App
import com.example.playlistmaker.settings.domain.model.AppSettings
import com.example.playlistmaker.settings.ui.models.SettingsState
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding?.let {
            it.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
                viewModel.setSettings(AppSettings(darkTheme = isChecked))
            }

            it.home.setOnClickListener {
                this.finish()
            }


            it.shareBtn.setOnClickListener {
                viewModel.sendShare(
                    getString(R.string.share_message),
                    getString(R.string.share_message_subj)
                )
            }
            it.supportBtn.setOnClickListener {
                viewModel.sendEmail(
                    EmailData(
                        mailBox = arrayOf(getString(R.string.support_message_email)),
                        subj = getString(R.string.support_message_subj),
                        text = getString(R.string.support_message_text),
                        data = "mailto:"
                    )
                )
            }
            it.agreementBtn.setOnClickListener {
                viewModel.sendUrl(getString(R.string.agreement_url))
            }
        }
        viewModel.getSettings()
    }

    private fun settingValuesSet(setting: AppSettings) {
        (applicationContext as App).switchTheme(setting.darkTheme)
    }

    private fun settingValuesGet(setting: AppSettings) {
        binding?.themeSwitcher?.isChecked = setting.darkTheme
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