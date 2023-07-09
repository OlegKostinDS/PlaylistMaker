package ru.dsvusial.playlistmaker.search.ui.view_model


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult
import ru.dsvusial.playlistmaker.search.ui.model.UiState
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private val recentHistoryTracks = ArrayList<TrackData>()
    private val executor: Executor = Executors.newCachedThreadPool()

    private val _uiStateLiveData = MutableLiveData<UiState>()
    fun observeUiStateLiveData(): LiveData<UiState> = _uiStateLiveData

    init {
        recentHistoryTracks.addAll(searchInteractor.getData())
        _uiStateLiveData.value = UiState.HistoryContent(recentHistoryTracks)
    }

    private val _textWatcherLiveData = MutableLiveData<Boolean>()
    fun observeTextWatcherStateLiveData(): LiveData<Boolean> = _textWatcherLiveData

    private val handler = Handler(Looper.getMainLooper())

    fun onClickClearHistoryBtn() {
        recentHistoryTracks.clear()
        searchInteractor.saveData(recentHistoryTracks)
        _uiStateLiveData.postValue(UiState.HistoryContent(recentHistoryTracks))
    }

    fun addToRecentHistoryList(trackData: TrackData) {
        when {
            (recentHistoryTracks.contains(trackData)) -> {
                recentHistoryTracks.remove(trackData)
                recentHistoryTracks.add(0, trackData)
            }

            (recentHistoryTracks.size < 10) -> {
                recentHistoryTracks.add(0, trackData)
            }

            else -> {
                recentHistoryTracks.removeAt(9)
                recentHistoryTracks.add(0, trackData)
            }

        }
        searchInteractor.saveData(recentHistoryTracks)


    }

    fun searchTextHasChanged(text: String) {
        _textWatcherLiveData.value = !text.isEmpty()
        if (text.isEmpty()) {
            _uiStateLiveData.value = UiState.HistoryContent(recentHistoryTracks)
        } else {
            searchDebounce(text)
        }
    }

    private fun searchDebounce(query: String) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ search(query) }, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun search(query: String) {
        _uiStateLiveData.value = UiState.Loading
        executor.execute {
            val result = searchInteractor.loadTracks(query)
            when (result) {
                is SearchResult.Success -> _uiStateLiveData.postValue(UiState.SearchContent(result.data!!))
                is SearchResult.Error -> _uiStateLiveData.postValue(UiState.Error(result.error!!))
            }
        }
    }


}
