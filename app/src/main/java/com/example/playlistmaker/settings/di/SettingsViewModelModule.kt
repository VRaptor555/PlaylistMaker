package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.sharing.di.sharingModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModule = module {
    viewModel {
        SettingsViewModel(get(), get(), get())
    }
}

val settingsModules = module {
    includes(settingsModule, settingsViewModule, sharingModule)
}