package ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors


import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {


    override fun pausePlayer() {
        mediaPlayerRepository.pausedPlayer()

    }

    override fun start() {
        mediaPlayerRepository.startPlayer()

    }

    override fun release() {
        mediaPlayerRepository.stopPlayer()
    }

    override fun prepare() {
        mediaPlayerRepository.preparePlayer()
    }

    override fun getPlayerState(): PlayerState {
        return mediaPlayerRepository.playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }


}