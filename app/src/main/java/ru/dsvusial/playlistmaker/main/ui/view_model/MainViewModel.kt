package ru.dsvusial.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.dsvusial.playlistmaker.creator.Creator
import ru.dsvusial.playlistmaker.main.ui.SingleLiveEvent
import ru.dsvusial.playlistmaker.main.ui.model.MainNavState
import ru.dsvusial.playlistmaker.search.ui.view_model.SearchViewModel

class MainViewModel : ViewModel() {

    val mainStatusLiveData = SingleLiveEvent<MainNavState>()
    fun getMainStatusLiveData(): LiveData<MainNavState> = mainStatusLiveData
    fun startSearchActivity() {
        mainStatusLiveData.value = MainNavState.SEARCH
    }

    fun startMusicLiblaryActivity() {
        mainStatusLiveData.value = MainNavState.MUSIC_LIBLARY
    }

    fun startSettingsActivity() {
        mainStatusLiveData.value = MainNavState.SETTINGS
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel()
            }
        }
    }
}