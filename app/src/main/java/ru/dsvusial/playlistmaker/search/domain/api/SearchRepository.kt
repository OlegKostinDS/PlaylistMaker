package ru.dsvusial.playlistmaker.search.domain.api

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

interface SearchRepository {

    fun clear()
    fun saveSearchHistory(historyTrack: ArrayList<TrackData>)
     fun loadTracks(query: String, onSuccess: (list: List<TrackData>) -> Unit, onError: (error: SearchUIType) -> Unit)
    fun getSearchHistory(): List<TrackData>
}