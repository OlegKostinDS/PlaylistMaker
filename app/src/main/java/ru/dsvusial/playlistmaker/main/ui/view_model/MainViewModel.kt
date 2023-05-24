package ru.dsvusial.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.dsvusial.playlistmaker.main.ui.SingleLiveEvent
import ru.dsvusial.playlistmaker.main.ui.model.MainNavState

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
}