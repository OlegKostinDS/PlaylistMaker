package ru.dsvusial.playlistmaker.mediaPlayer.domain.repository

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState

interface MediaPlayerRepository{
    fun preparePlayer(listenerState : (PlayerState) -> Unit)
    fun startPlayer()
    fun pausedPlayer()
    fun stopPlayer()
    fun getCurrentPosition(): Int
    var playerState: PlayerState

}