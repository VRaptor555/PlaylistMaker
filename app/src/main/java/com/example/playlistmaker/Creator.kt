package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.History
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.dto.HistoryRepository
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.presentation.TracksSearchHistoryImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSharedHistory(sharedPreferences: SharedPreferences, historyKey: String): History {
        return HistoryRepository(sharedPreferences, historyKey)
    }

    fun provideHistory(sharedPreferences: SharedPreferences, historyKey: String): TracksHistoryInteractor {
        return TracksSearchHistoryImpl(getSharedHistory(sharedPreferences, historyKey))
    }
}