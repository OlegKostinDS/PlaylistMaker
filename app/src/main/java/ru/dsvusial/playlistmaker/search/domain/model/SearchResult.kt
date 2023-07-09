package ru.dsvusial.playlistmaker.search.domain.model

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

sealed class SearchResult(
    val data: List<TrackData>? = null,
    val error: SearchUIType? = null
) {
    class Success(data: List<TrackData>) : SearchResult(data = data) {

    }

    class Error(error: SearchUIType) : SearchResult(error = error)
}