package ru.dsvusial.playlistmaker.search.domain.api

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult

interface SearchRepository {

    fun clear()
    fun saveSearchHistory(historyTrack: ArrayList<TrackData>)
     fun loadTracks(query: String): SearchResult
    fun getSearchHistory(): List<TrackData>
}