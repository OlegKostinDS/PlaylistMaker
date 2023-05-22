package ru.dsvusial.playlistmaker.mediaPlayer.domain.repository

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState

interface MediaPlayerRepository{
    fun preparePlayer(trackUrl : String)
    fun startPlayer()
    fun pausedPlayer()
    fun stopPlayer()
    fun getCurrentPosition(): Int
    fun getPlayState() : PlayerState


}