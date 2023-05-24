package ru.dsvusial.playlistmaker.di

import org.koin.dsl.module
import ru.dsvusial.playlistmaker.mediaPlayer.data.MediaPlayerRepositoryImpl
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository
import ru.dsvusial.playlistmaker.search.data.repository.SearchRepositoryImpl
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.settings.data.SettingsRepositoryImpl
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val repositoryModule = module {
    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
}



