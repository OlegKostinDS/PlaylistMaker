package ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.ui.PlayStatus

class MediaPlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    private val durationLiveData = MutableLiveData<String>()
    fun getDurationLiveData(): LiveData<String> = durationLiveData

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
        handler.removeCallbacksAndMessages(null)
    }

    fun onViewPaused() {
        mediaPlayerInteractor.pausePlayer()
        playStatusLiveData.postValue(PlayStatus.OnPause)
        handler.removeCallbacksAndMessages(null)
    }

    fun onPlayBtnClicked() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                onViewPaused()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerState.STATE_DEFAULT -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.start()
        playStatusLiveData.postValue(PlayStatus.OnStart)
        handler.postDelayed(object : Runnable {
            override fun run() {
                durationLiveData.value = mediaPlayerInteractor.getCurrentPosition()

                val state = mediaPlayerInteractor.getPlayerState()
                if (state == PlayerState.STATE_PREPARED) {
                    durationLiveData.value = "00:00"
                    playStatusLiveData.postValue(PlayStatus.OnPause)
                    handler.removeCallbacksAndMessages(null)
                }
                handler.postDelayed(this, MP_DELAY)
            }
        }, MP_DELAY)
    }

    fun preparePlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(trackUrl)
    }


    companion object {
        private val MP_DELAY = 1000L
    }
}