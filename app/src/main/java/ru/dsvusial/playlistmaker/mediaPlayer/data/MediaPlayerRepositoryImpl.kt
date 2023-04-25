package ru.dsvusial.playlistmaker.mediaPlayer.data

import android.media.MediaPlayer
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.PlayerState
import ru.dsvusial.playlistmaker.mediaPlayer.domain.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val trackUrl: String) :
    MediaPlayerRepository {
  override  var playerState = PlayerState.STATE_DEFAULT
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    override fun preparePlayer() {
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

    override fun pausedPlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }


}