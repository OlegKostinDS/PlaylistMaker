package ru.dsvusial.playlistmaker.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.dsvusial.playlistmaker.addPlaylist.ui.viewmodel.AddPlaylistViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model.MediaPlayerViewModel
import ru.dsvusial.playlistmaker.music_library.ui.view_models.FavoritesViewModel
import ru.dsvusial.playlistmaker.music_library.ui.view_models.PlaylistViewModel
import ru.dsvusial.playlistmaker.search.ui.view_model.SearchViewModel
import ru.dsvusial.playlistmaker.settings.ui.view_model.SettingViewModel

val viewModelModule = module {
    viewModel {
        MediaPlayerViewModel(get(),get(),get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        SettingViewModel(get(), androidApplication())
    }


    viewModel {
        FavoritesViewModel(get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
    viewModel{
        AddPlaylistViewModel(get())
    }
}
