package ru.dsvusial.playlistmaker.network

data class TrackData (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String

)