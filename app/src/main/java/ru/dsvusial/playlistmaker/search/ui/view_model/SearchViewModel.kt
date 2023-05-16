package ru.dsvusial.playlistmaker.search.ui.view_model


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dsvusial.playlistmaker.mediaPlayer.creator.Creator
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.network.TrackResponse
import ru.dsvusial.playlistmaker.search.domain.api.SearchInteractor
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType
import ru.dsvusial.playlistmaker.search.ui.model.UiState


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private val recentHistoryTracks = ArrayList<TrackData>()

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
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(searchInteractor = Creator.provideSearchInteractor())
            }
        }
    }

     fun search(query: String) {
        _uiStateLiveData.value = UiState.Loading
      //  progressbar.visibility = View.VISIBLE
        searchInteractor.loadTracks(query,
            onSuccess = {
            _uiStateLiveData.value = UiState.SearchContent(it)
        }, onError = {
            _uiStateLiveData.value = UiState.Error(error = it)
        })

//        songService.search(searchEditText.text.toString())
//            .enqueue(object : Callback<TrackResponse> {
//                override fun onResponse(
//                    call: Call<TrackResponse>,
//                    response: Response<TrackResponse>
//                ) {
//                    when (response.code()) {
//                        200 ->
//                            if (response.body()?.results?.isNotEmpty() == true) {
//                                selectSearchUI(SearchUIType.SUCCESS)
//                                tracks.clear()
//                                tracks.addAll(response.body()?.results!!)
//                                searchTracksRecyclerView.adapter?.notifyDataSetChanged()
//
//                            } else {
//                                selectSearchUI(SearchUIType.NO_DATA)
//
//                            }
//
//                        else -> {
//                            selectSearchUI(SearchUIType.NO_INTERNET)
//                        }
//                    }
//                }
//
//
//                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
//                    selectSearchUI(SearchUIType.NO_INTERNET)
//
//                }
//
//            })
    }


}
