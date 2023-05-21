package ru.dsvusial.playlistmaker.creator

import android.content.SharedPreferences
import ru.dsvusial.playlistmaker.App
import ru.dsvusial.playlistmaker.mediaPlayer.data.MediaPlayerRepositoryImpl
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractorImpl
import ru.dsvusial.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.dsvusial.playlistmaker.search.data.repository.SearchRepositoryImpl
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.search.domain.impl.SearchInteractorImpl
import ru.dsvusial.playlistmaker.settings.data.SettingsRepositoryImpl
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository
import ru.dsvusial.playlistmaker.settings.domain.impl.SettingsInteractor
import ru.dsvusial.playlistmaker.settings.domain.impl.SettingsInteractorImpl

object Creator {


    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(
            MediaPlayerRepositoryImpl()
        )
    }


    fun getSharedPreferences(): SharedPreferences {
        return App.instance.sharedPreferences
    }

    fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(
            sharedPreferences = getSharedPreferences(),
            networkClient = RetrofitNetworkClient()
        )
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(searchRepository = getSearchRepository())
    }

    fun getSettingRepository(): SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences = getSharedPreferences())
    }

    fun provideSettingInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(settingRepository = getSettingRepository())

    }
}