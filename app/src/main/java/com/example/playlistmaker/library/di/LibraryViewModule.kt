package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistDetailViewModel
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val libraryView = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        PlaylistCreateViewModel(get())
    }

    viewModel { (playlist: Playlist) ->
        PlaylistDetailViewModel(playlist, get())
    }


}

val libraryModules = module {
    includes(libraryDbModule, libraryView)
}