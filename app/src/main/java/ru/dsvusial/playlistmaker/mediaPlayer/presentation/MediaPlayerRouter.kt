package ru.dsvusial.playlistmaker.mediaPlayer.presentation

import androidx.appcompat.app.AppCompatActivity

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY

class MediaPlayerRouter (private val activity: AppCompatActivity){
    fun getTrack(): TrackData{
        return activity.intent.getSerializableExtra(SEARCH_KEY)!! as TrackData
    }
}