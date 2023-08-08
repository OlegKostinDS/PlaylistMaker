package ru.dsvusial.playlistmaker.di

import org.koin.dsl.module
import ru.dsvusial.playlistmaker.addPlaylist.data.PlaylistRepositoryImpl
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistRepository
import ru.dsvusial.playlistmaker.detailedPlaylist.data.DetailedPlaylistRepositoryImpl
import ru.dsvusial.playlistmaker.detailedPlaylist.domain.DetailedPlaylistRepository
import ru.dsvusial.playlistmaker.mediaPlayer.data.MediaPlayerRepositoryImpl
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository
import ru.dsvusial.playlistmaker.music_library.data.FavoritesTrackRepositoryImpl
import ru.dsvusial.playlistmaker.music_library.domain.db.FavoritesTrackRepository
import ru.dsvusial.playlistmaker.search.data.repository.SearchRepositoryImpl
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.settings.data.SettingsRepositoryImpl
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository

val repositoryModule = module {
    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<FavoritesTrackRepository> {
        FavoritesTrackRepositoryImpl(get(),get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(get(),get(),get(),get())
    }
    single<PlaylistRepository> {
      PlaylistRepositoryImpl(get(),get(),get())
    }

    single<DetailedPlaylistRepository>{
        DetailedPlaylistRepositoryImpl(get(),get(),get())
    }
}



