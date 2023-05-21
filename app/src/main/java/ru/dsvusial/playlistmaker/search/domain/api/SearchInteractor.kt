package ru.dsvusial.playlistmaker.search.domain.api

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

interface SearchInteractor {
    fun clearHistory()
    fun saveData(historyList: ArrayList<TrackData>)
    fun getData():List<TrackData>
    fun loadTracks(query: String, onSuccess: (list: List<TrackData>)-> Unit, onError: (error: SearchUIType)-> Unit)
}