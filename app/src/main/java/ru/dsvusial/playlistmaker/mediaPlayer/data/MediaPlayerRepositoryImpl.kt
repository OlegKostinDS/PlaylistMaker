package ru.dsvusial.playlistmaker.mediaPlayer.data

import android.media.MediaPlayer
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl() :
    MediaPlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = null
    override fun preparePlayer(trackUrl: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.apply {
            setDataSource(trackUrl)
            prepare()
            playerState = PlayerState.STATE_PREPARED
            setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
            }
        }
    }

    override fun startPlayer(trackUrl: String) {
        if (playerState == PlayerState.STATE_DEFAULT) {
            preparePlayer(trackUrl)
        }
        mediaPlayer?.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun stopPlayer() {
        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }

    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun getPlayState(): PlayerState {
        return playerState
    }

    override fun pausedPlayer() {
        mediaPlayer?.pause()
        playerState = PlayerState.STATE_PAUSED
    }

}