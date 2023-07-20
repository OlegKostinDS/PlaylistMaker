package ru.dsvusial.playlistmaker.di

import org.koin.dsl.module
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractorImpl
import ru.dsvusial.playlistmaker.music_library.domain.db.TrackInteractor
import ru.dsvusial.playlistmaker.music_library.domain.impl.TrackInteractorImpl
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.impl.SearchInteractorImpl
import ru.dsvusial.playlistmaker.settings.domain.impl.SettingsInteractor
import ru.dsvusial.playlistmaker.settings.domain.impl.SettingsInteractorImpl

val interactorModule = module {
    single<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }
    single<SearchInteractor>{
        SearchInteractorImpl(get())
    }

    single<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }
    single<TrackInteractor>{
        TrackInteractorImpl(get())
    }
}