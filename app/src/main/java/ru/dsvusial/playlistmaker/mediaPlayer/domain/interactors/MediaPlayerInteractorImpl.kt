package ru.dsvusial.playlistmaker.mediaPlayer.domain.interactors


import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository
import ru.dsvusial.playlistmaker.utils.DateTimeUtil

class MediaPlayerInteractorImpl(
    val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {


    override fun pausePlayer() {
        mediaPlayerRepository.pausedPlayer()

    }

    override fun start(trackUrl: String) {
        mediaPlayerRepository.startPlayer(trackUrl)

    }

    override fun release() {
        mediaPlayerRepository.stopPlayer()
    }

    override fun prepare(trackUrl: String) {
        mediaPlayerRepository.preparePlayer(trackUrl)
    }

    override fun getPlayerState(): PlayerState {
        return mediaPlayerRepository.getPlayState()
    }

    override fun getCurrentPosition(): String {
        return convertMillisecondsToString(mediaPlayerRepository.getCurrentPosition())
    }

    private fun convertMillisecondsToString(duration: Int): String {
        return DateTimeUtil.formatTimeMillisToString(duration)
    }

}