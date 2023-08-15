package ru.dsvusial.playlistmaker.detailedPlaylist.ui.model

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

sealed class BottomSheetState {
    data class Content(
        val playlists: List<TrackData>
    ) : BottomSheetState()

    object Empty : BottomSheetState()
}