package ru.dsvusial.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult

interface SearchRepository {

    fun clear()
    suspend fun saveSearchHistory(historyTrack: ArrayList<TrackData>)
       fun loadTracks(query: String): Flow<SearchResult>
    fun getSearchHistory(): List<TrackData>
}