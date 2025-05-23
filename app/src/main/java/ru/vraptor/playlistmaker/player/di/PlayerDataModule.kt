package ru.vraptor.playlistmaker.player.di

import android.media.MediaPlayer
import ru.vraptor.playlistmaker.player.domain.PlayerRepository
import ru.vraptor.playlistmaker.player.data.impl.PlayerRepositoryImpl
import org.koin.dsl.module

val playerDataModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single {
        MediaPlayer()
    }
}