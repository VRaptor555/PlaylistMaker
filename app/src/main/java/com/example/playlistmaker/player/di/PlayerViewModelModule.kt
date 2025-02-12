package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModule = module {
    viewModel { (url: String?) ->
        PlayerViewModel(url, get(), get())
    }
}

val playerModules = module {
    includes(playerDataModule, playerModule, playerViewModule)
}