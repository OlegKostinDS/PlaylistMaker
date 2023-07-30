package ru.dsvusial.playlistmaker.music_library.ui.model

import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData

sealed interface  PlaylistState {
    data class Content(
        val playlists: List<PlaylistData>
    ) : PlaylistState

    object Empty : PlaylistState
}