package ru.dsvusial.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {
    override fun clearHistory() {
        searchRepository.clear()
    }

    override suspend fun saveData(historyList: ArrayList<TrackData>) {
        searchRepository.saveSearchHistory(historyList)
    }

    override fun getData(): List<TrackData> {
        return searchRepository.getSearchHistory()
    }

    override fun loadTracks(
        query: String,
        ) : Flow<SearchResult> {
      return searchRepository.loadTracks(query)
    }
}