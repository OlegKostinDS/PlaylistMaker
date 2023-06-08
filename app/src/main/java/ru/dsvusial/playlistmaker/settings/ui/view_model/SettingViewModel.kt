package ru.dsvusial.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dsvusial.playlistmaker.App
import ru.dsvusial.playlistmaker.settings.domain.impl.SettingsInteractor
import ru.dsvusial.playlistmaker.utils.THEME_KEY

class SettingViewModel(
    val settingsInteractor: SettingsInteractor,
    private val application: Application
) : AndroidViewModel(application) {
    val switcherStatusLiveData = MutableLiveData<Boolean>()
    fun getSwitcherStatusLiveData(): LiveData<Boolean> = switcherStatusLiveData
    init {
        switcherStatusLiveData.value = settingsInteractor.getTheme(THEME_KEY)
    }
    fun onSwitcherClicked(status: Boolean) {
        settingsInteractor.saveTheme(THEME_KEY, status)
        switcherStatusLiveData.value = status
        (application as App).switchTheme(status)

    }
}