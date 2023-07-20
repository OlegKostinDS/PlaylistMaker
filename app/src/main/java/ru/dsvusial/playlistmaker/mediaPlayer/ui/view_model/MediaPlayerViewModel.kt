package ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors.MediaPlayerInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.mediaPlayer.ui.PlayStatus
import ru.dsvusial.playlistmaker.music_library.domain.db.TrackInteractor

class MediaPlayerViewModel(
    val mediaPlayerInteractor: MediaPlayerInteractor,
    val trackInteractor: TrackInteractor
) : ViewModel() {
    private var timerJob: Job? = null

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    private val durationLiveData = MutableLiveData<String>()
    fun getDurationLiveData(): LiveData<String> = durationLiveData

    private val favoritesLiveData = MutableLiveData<Boolean>()
    fun getFavoritesLiveData(): LiveData<Boolean> = favoritesLiveData

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()

    }

    fun onViewPaused() {
        mediaPlayerInteractor.pausePlayer()
        playStatusLiveData.postValue(PlayStatus.OnPause)
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

    fun onFavBtnClicked(trackData: TrackData) {
        viewModelScope.launch {
            if (trackData.isFavorite) {
                trackData.isFavorite = false
                favoritesLiveData.value = false
                trackInteractor.unputFavoriteTrack(trackData)
            } else {
                trackData.isFavorite = true
                favoritesLiveData.value = true
                trackInteractor.putFavoriteTrack(trackData)

            }
        }

    }

    fun isFavorite(trackId:String){
        viewModelScope.launch {
            trackInteractor.getFavoriteIds(trackId).collect { trackId ->
                favoritesLiveData.value = trackId
            }
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
                delay(MP_DELAY_MS)
                state = mediaPlayerInteractor.getPlayerState()
            }
            if (state == PlayerState.STATE_PREPARED) {
                durationLiveData.value = "00:00"
                playStatusLiveData.postValue(PlayStatus.OnPause)
            }
        }
    }

    fun preparePlayer(trackUrl: String) {
        mediaPlayerInteractor.prepare(trackUrl)
    }


    companion object {
        private const val MP_DELAY_MS = 1000L
    }
}