package ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.ui.PlayStatus

class MediaPlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private var timerJob: Job? = null

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

    fun onPlayBtnClicked(trackUrl: String) {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                onViewPaused()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer(trackUrl)
            }
            PlayerState.STATE_DEFAULT -> startPlayer(trackUrl)
        }
    }

    private fun startPlayer(trackUrl: String) {
        mediaPlayerInteractor.start(trackUrl)
        playStatusLiveData.postValue(PlayStatus.OnStart)

        timerJob = viewModelScope.launch {
            durationLiveData.value = mediaPlayerInteractor.getCurrentPosition()
            var state = mediaPlayerInteractor.getPlayerState()

                while (state == PlayerState.STATE_PLAYING) {
                    durationLiveData.value = mediaPlayerInteractor.getCurrentPosition()
                    delay(MP_DELAY)
                    state = mediaPlayerInteractor.getPlayerState()
                }
            if (state == PlayerState.STATE_PREPARED) {
                durationLiveData.value = "00:00"
                playStatusLiveData. postValue(PlayStatus.OnPause)
                timerJob?.cancel()
            }
        }
    }

    fun preparePlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(trackUrl)
    }


    companion object {
        private const val MP_DELAY = 1000L
    }
}