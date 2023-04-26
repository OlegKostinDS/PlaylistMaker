package ru.dsvusial.playlistmaker.mediaPlayer.presentation

import android.os.Handler
import android.os.Looper
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.*


class MediaPlayerPresenterImpl(
    val mediaPlayerView: MediaPlayerView,
    val mediaPlayerInteractor: MediaPlayerInteractor,
    val router: Router
) {
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private val MP_DELAY = 1000L
    }

    init {
        mediaPlayerView.getData(router.getTrack())
        mediaPlayerInteractor.prepare()
    }


    fun onViewPaused() {
        mediaPlayerInteractor.pausePlayer()
        mediaPlayerView.setStartImage()
        handler.removeCallbacksAndMessages(null)
    }

    fun onViewDestroyed() {
        mediaPlayerInteractor.release()
        handler.removeCallbacksAndMessages(null)
    }

    fun onBackPressed() {
        mediaPlayerView.goBack()
    }

    fun onPlayBtnClicked() {

        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerState.STATE_DEFAULT -> {
                mediaPlayerInteractor.prepare()
                startPlayer()
            }
        }
    }


    private fun startPlayer() {
        mediaPlayerInteractor.start()
        mediaPlayerView.setPausedImage()


        handler.postDelayed(object : Runnable {
            override fun run() {

                mediaPlayerView.setDuration(convertMillisecondsToString(mediaPlayerInteractor.getCurrentPosition()))
                val state = mediaPlayerInteractor.getPlayerState()
                if (state == PlayerState.STATE_PREPARED) {
                    mediaPlayerView.setStartImage()
                    mediaPlayerView.setDuration("00:00")
                    handler.removeCallbacksAndMessages(null)
                }
                handler.postDelayed(this, MP_DELAY)
            }
        }, MP_DELAY)
    }

    private fun convertMillisecondsToString(duration: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        mediaPlayerView.setStartImage()
        handler.removeCallbacksAndMessages(null)
    }


}