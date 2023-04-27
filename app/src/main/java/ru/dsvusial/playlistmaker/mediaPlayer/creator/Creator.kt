package ru.dsvusial.playlistmaker.mediaPlayer.creator

import androidx.appcompat.app.AppCompatActivity
import ru.dsvusial.playlistmaker.mediaPlayer.data.MediaPlayerRepositoryImpl
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractorImpl
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerPresenterImpl
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerView
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.Router

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
}