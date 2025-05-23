package ru.vraptor.playlistmaker.library.di

import ru.vraptor.playlistmaker.library.domain.model.Playlist
import ru.vraptor.playlistmaker.library.ui.view_model.FavoriteViewModel
import ru.vraptor.playlistmaker.library.ui.view_model.PlaylistCreateViewModel
import ru.vraptor.playlistmaker.library.ui.view_model.PlaylistDetailViewModel
import ru.vraptor.playlistmaker.library.ui.view_model.PlaylistEditViewModel
import ru.vraptor.playlistmaker.library.ui.view_model.PlaylistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val libraryView = module {

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel { (playlist: Playlist) ->
        PlaylistCreateViewModel(playlist, get())
    }

    viewModel { (playlist: Playlist) ->
        PlaylistEditViewModel(playlist, get())
    }

    viewModel { (playlist: Playlist) ->
        PlaylistDetailViewModel(playlist, get(), get(), get())
    }

}

val libraryModules = module {
    includes(libraryDbModule, libraryView)
}