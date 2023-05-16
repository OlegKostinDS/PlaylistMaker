package ru.dsvusial.playlistmaker.mediaPlayer.creator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import ru.dsvusial.playlistmaker.App
import ru.dsvusial.playlistmaker.mediaPlayer.data.MediaPlayerRepositoryImpl
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractorImpl
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerPresenterImpl
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerView
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.Router
import ru.dsvusial.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.dsvusial.playlistmaker.search.data.repository.SearchRepositoryImpl
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.search.domain.impl.SearchInteractorImpl

object Creator {
    fun providePresenter(
        mediaPlayerView: MediaPlayerView,
        activity: AppCompatActivity
    ): MediaPlayerPresenterImpl {
        val router = Router(activity)
        return MediaPlayerPresenterImpl(
            mediaPlayerView = mediaPlayerView,
            mediaPlayerInteractor = MediaPlayerInteractorImpl(
                MediaPlayerRepositoryImpl(router.getTrack().previewUrl)
            ),
            router = router
        )
    }

    fun getSharedPreferences(): SharedPreferences {
        return App.instance.sharedPreferences
    }

    fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(sharedPreferences = getSharedPreferences(), networkClient = RetrofitNetworkClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(searchRepository = getSearchRepository())
    }
}