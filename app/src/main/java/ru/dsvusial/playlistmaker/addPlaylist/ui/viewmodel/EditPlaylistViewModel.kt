package ru.dsvusial.playlistmaker.addPlaylist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistIncteractor
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData

class EditPlaylistViewModel(playlistIncteractor: PlaylistIncteractor) :
    AddPlaylistViewModel(playlistIncteractor) {
    val playlistLiveData: MutableLiveData<PlaylistData> = MutableLiveData()
    fun getPlaylistLiveData(): LiveData<PlaylistData> = playlistLiveData

    fun getData(playlistData: PlaylistData) {
        playlistLiveData.value = playlistData
    }


    fun updatePlaylistForEdit(playlistData: PlaylistData) {
        viewModelScope.launch {
          playlistIncteractor.updatePlaylistForEdit(playlistData)
        }

    }


}