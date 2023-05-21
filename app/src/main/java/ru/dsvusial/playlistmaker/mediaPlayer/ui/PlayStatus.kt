package ru.dsvusial.playlistmaker.mediaPlayer.ui

sealed class PlayStatus {
    object OnStart : PlayStatus()
    object OnPause : PlayStatus()
}