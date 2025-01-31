package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.main.ui.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.search.data.HistoryRepository
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.search.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.dto.TracksListHistoryStorage
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.dto.SettingsStorage
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun getSharedHistory(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(TracksListHistoryStorage(
            context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
        ))
    }

    fun provideHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(getSharedHistory(context))
    }

    private fun getPlayerRepository(url: String?): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer(), url)
    }

    fun providePlayer(url: String?): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(url))
    }

    private fun getSettings(application: Application): SettingsRepository {
        return SettingsRepositoryImpl(SettingsStorage(application))
    }

    fun provideSettings(application: Application): SettingsInteractor {
        return SettingsInteractorImpl(getSettings(application))
    }
}