package ru.vraptor.playlistmaker.settings.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.vraptor.playlistmaker.R
import ru.vraptor.playlistmaker.databinding.FragmentSettingsBinding
import ru.vraptor.playlistmaker.main.ui.App
import ru.vraptor.playlistmaker.main.ui.fragments.BindingFragments
import ru.vraptor.playlistmaker.settings.domain.model.AppSettings
import ru.vraptor.playlistmaker.settings.ui.models.SettingsState
import ru.vraptor.playlistmaker.settings.ui.view_model.SettingsViewModel
import ru.vraptor.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: BindingFragments<FragmentSettingsBinding>() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        with(binding) {
            themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
                viewModel.setSettings(AppSettings(darkTheme = isChecked))
            }

            shareBtn.setOnClickListener {
                viewModel.sendShare(
                    getString(R.string.share_message),
                    getString(R.string.share_message_subj)
                )
            }
            supportBtn.setOnClickListener {
                viewModel.sendEmail(
                    EmailData(
                        mailBox = arrayOf(getString(R.string.support_message_email)),
                        subj = getString(R.string.support_message_subj),
                        text = getString(R.string.support_message_text),
                        data = "mailto:"
                    )
                )
            }
            agreementBtn.setOnClickListener {
                viewModel.sendUrl(getString(R.string.agreement_url))
            }
        }
        viewModel.getSettings()
    }

    private fun settingValuesSet(setting: AppSettings) {
        (requireContext().applicationContext as App).switchTheme(setting.darkTheme)
    }

    private fun settingValuesGet(setting: AppSettings) {
        binding.themeSwitcher.isChecked = setting.darkTheme
        (requireContext().applicationContext as App).switchTheme(setting.darkTheme)
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