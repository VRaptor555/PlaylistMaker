package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryView = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        PlaylistViewModel()
    }

}

val libraryModules = module {
    includes(libraryView)
}