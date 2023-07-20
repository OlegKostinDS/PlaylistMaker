package ru.dsvusial.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult

interface SearchInteractor {
    fun clearHistory()
   suspend fun saveData(historyList: ArrayList<TrackData>)
    fun getData():List<TrackData>
    fun loadTracks(query: String): Flow< SearchResult>
}