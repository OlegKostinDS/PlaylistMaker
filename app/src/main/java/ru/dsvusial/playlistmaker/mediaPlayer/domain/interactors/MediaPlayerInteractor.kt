package ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState

interface MediaPlayerInteractor {
    fun pausePlayer()
    fun start(trackUrl: String)
    fun release()
    fun prepare(trackUrl: String)
    fun getPlayerState(): PlayerState
    fun getCurrentPosition(): String
}