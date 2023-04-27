package ru.dsvusial.playlistmaker.mediaPlayer.presentation

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface MediaPlayerView {
   fun  getData(track: TrackData)
   fun setPausedImage()
   fun setStartImage()
   fun goBack()
   fun setDuration(duration : String)
}