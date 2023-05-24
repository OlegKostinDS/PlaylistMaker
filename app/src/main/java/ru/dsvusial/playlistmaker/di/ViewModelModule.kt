package ru.dsvusial.playlistmaker.di

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.dsvusial.playlistmaker.main.ui.view_model.MainViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model.MediaPlayerViewModel
import ru.dsvusial.playlistmaker.search.ui.view_model.SearchViewModel
import ru.dsvusial.playlistmaker.settings.ui.view_model.SettingViewModel

val viewModelModule = module {
    viewModel {
        MediaPlayerViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        SettingViewModel(get(), androidApplication())
    }

    viewModel{
        MainViewModel()
    }
}
