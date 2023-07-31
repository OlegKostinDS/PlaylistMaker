package ru.dsvusial.playlistmaker.addPlaylist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistIncteractor
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData

class AddPlaylistViewModel(val playlistIncteractor: PlaylistIncteractor) : ViewModel() {

    private val buttonStatusLiveData = MutableLiveData<Boolean>()
    fun getButtonStatusLiveData(): LiveData<Boolean> = buttonStatusLiveData


    fun nameTextHasChanged(status: Boolean) {
        buttonStatusLiveData.value = status
    }

    fun addPlaylist(playlist: PlaylistData) {
        viewModelScope.launch {
            playlistIncteractor.addPlaylist(playlist)
        }
    }




}