package ru.dsvusial.playlistmaker.search.ui.model


import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

sealed interface UiState {

    object Loading : UiState

    data class SearchContent(val list: List<TrackData>) : UiState

    data class HistoryContent(val list: List<TrackData>) : UiState

    data class Error(val error: SearchUIType) : UiState
}