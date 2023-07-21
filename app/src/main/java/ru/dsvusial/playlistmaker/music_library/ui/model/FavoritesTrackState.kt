package ru.dsvusial.playlistmaker.music_library.ui.model

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

sealed interface FavoritesTrackState {


    data class Content(
        val tracks: List<TrackData>
    ) : FavoritesTrackState

    object Empty : FavoritesTrackState
}