package ru.vraptor.playlistmaker.player.di

import ru.vraptor.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.vraptor.playlistmaker.search.domain.model.Track
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerViewModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get(), get())
    }
}

val playerModules = module {
    includes(playerDataModule, playerModule, playerViewModule)
}