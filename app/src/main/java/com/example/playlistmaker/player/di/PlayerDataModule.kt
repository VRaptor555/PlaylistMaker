package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import org.koin.dsl.module

val playerDataModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single {
        MediaPlayer()
    }
}