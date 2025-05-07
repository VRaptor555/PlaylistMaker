package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get(), get())
    }
}

val playerModules = module {
    includes(playerDataModule, playerModule, playerViewModule)
}