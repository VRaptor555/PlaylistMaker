package ru.vraptor.playlistmaker.settings.di

import ru.vraptor.playlistmaker.settings.ui.view_model.SettingsViewModel
import ru.vraptor.playlistmaker.sharing.di.sharingModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsViewModule = module {
    viewModel {
        SettingsViewModel(get(), get(), get())
    }
}

val settingsModules = module {
    includes(settingsModule, settingsViewModule, sharingModule)
}