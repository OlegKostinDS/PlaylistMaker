package ru.dsvusial.playlistmaker.mediaPlayer.data

import android.media.MediaPlayer
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) :
    MediaPlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(trackUrl: String) {
        mediaPlayer.apply {
            setDataSource(trackUrl)
            prepare()
            playerState = PlayerState.STATE_PREPARED
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun stopPlayer() {
        mediaPlayer.release()

    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getPlayState(): PlayerState {
        return playerState
    }

    override fun pausedPlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

}