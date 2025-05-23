package ru.vraptor.playlistmaker.search.di

import androidx.room.Room
import ru.vraptor.playlistmaker.search.domain.db.FavoriteRepository
import ru.vraptor.playlistmaker.search.data.db.FavoriteRepositoryImpl
import ru.vraptor.playlistmaker.search.data.converters.TrackDbConverter
import ru.vraptor.playlistmaker.search.data.db.AppDatabase
import ru.vraptor.playlistmaker.search.domain.db.FavoriteInteractor
import ru.vraptor.playlistmaker.search.domain.impl.FavoriteInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val historyDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single {
        TrackDbConverter()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}