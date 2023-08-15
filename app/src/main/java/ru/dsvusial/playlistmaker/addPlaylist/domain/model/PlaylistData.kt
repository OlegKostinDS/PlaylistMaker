package ru.dsvusial.playlistmaker.addPlaylist.domain.model

data class PlaylistData(
    val id: Int,
    val playlistName: String,
    val playlistDesc: String?,
    val playlistUri: String,
    val playlistTracks: List<String>,
    val playlistAmount: Int
) : java.io.Serializable