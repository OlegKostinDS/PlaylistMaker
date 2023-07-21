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
                trackInteractor.unputFavoriteTrack(trackData)
                trackData.isFavorite = false
                favoritesLiveData.value = false
            } else {
                trackInteractor.putFavoriteTrack(trackData)
                trackData.isFavorite = true
                favoritesLiveData.value = true
            }
        }

    }

    fun isFavorite(track: TrackData) {
        viewModelScope.launch {
            trackInteractor.getFavoriteIds(track.trackId).collect { isFavTrack ->
                favoritesLiveData.postValue(isFavTrack)
                track.isFavorite = isFavTrack
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