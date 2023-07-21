package ru.dsvusial.playlistmaker.music_library.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.domain.db.TrackInteractor
import ru.dsvusial.playlistmaker.music_library.ui.model.FavoritesTrackState


class FavoritesViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {
    init {
        fillData()
    }

    private val stateLiveData = MutableLiveData<FavoritesTrackState>()

    fun observeState(): LiveData<FavoritesTrackState> = stateLiveData

    private fun fillData() {

        viewModelScope.launch {
            trackInteractor
                .getFavoritesTracks()
                .collect { tracks ->
                    processResult(tracks.reversed())
                }
        }

    }

    private fun processResult(tracks: List<TrackData>) {
        if (tracks.isEmpty()) {
            renderState(FavoritesTrackState.Empty)
        } else {
            renderState(FavoritesTrackState.Content(tracks))
        }
    }

    private fun renderState(state: FavoritesTrackState) {
        stateLiveData.postValue(state)
    }


}