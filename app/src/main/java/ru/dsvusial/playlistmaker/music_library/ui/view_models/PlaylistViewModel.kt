package ru.dsvusial.playlistmaker.music_library.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistIncteractor
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.music_library.ui.model.PlaylistState

class PlaylistViewModel(val playlistIncteractor: PlaylistIncteractor) : ViewModel() {
    init {
        fillData()
    }

    private val stateLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = stateLiveData
    private fun fillData() {

        viewModelScope.launch {
            playlistIncteractor
                .getPlaylists()
                .collect { playlist ->
                    processResult(playlist)
                }
        }

    }

    private fun processResult(playlists: List<PlaylistData>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistState.Empty)
        } else {
            renderState(PlaylistState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }
}