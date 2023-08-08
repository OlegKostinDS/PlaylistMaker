package ru.dsvusial.playlistmaker.detailedPlaylist.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistIncteractor
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.detailedPlaylist.domain.DetailedPlaylistInteractor
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData


class DetailedPlaylistViewModel(
    val playlistIncteractor: PlaylistIncteractor,
    private val tracksForPlaylistInteractor: DetailedPlaylistInteractor
) : ViewModel() {


    private val tracksLiveData = MutableLiveData<List<TrackData>>()
    fun getTracks(): LiveData<List<TrackData>> = tracksLiveData
    private val playlistLiveData = MutableLiveData<PlaylistData>()
    fun getPlaylist(): LiveData<PlaylistData> = playlistLiveData

    fun fillData(playlistData: PlaylistData) {

        viewModelScope.launch {
            tracksForPlaylistInteractor.getPlaylistForId(playlistData.id).collect {
                playlistLiveData.value = it
            }
        }
        viewModelScope.launch {
            tracksForPlaylistInteractor
                .getTracksForPlaylist(playlistData.playlistTracks)
                .collect { tracks ->
                    tracksLiveData.value = tracks
                }
        }


    }

    fun deleteTrackById(trackId: String, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistIncteractor.deleteTrackFromPlaylistById(trackId, playlistId).collect { tracks ->
                withContext(Dispatchers.Main) {
                    tracksLiveData.value = tracks
                }

            }
        }
    }

    fun deletePlaylist(playlistData: PlaylistData) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistIncteractor.deletePlaylist(playlistData)
        }
    }


}