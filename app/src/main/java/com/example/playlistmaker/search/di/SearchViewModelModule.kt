package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.view_model.TrackSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModule = module {
    viewModel {
        TrackSearchViewModel(get(), get(), get())
    }
}

val searchModules = module {
    includes(searchDataModule, searchModule, searchViewModule)
}