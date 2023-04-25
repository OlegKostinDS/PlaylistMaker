package ru.dsvusial.playlistmaker.mediaPlayer.presentation

import androidx.appcompat.app.AppCompatActivity
import ru.dsvusial.playlistmaker.mediaPlayer.ui.SEARCH_KEY
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

class Router (private val activity: AppCompatActivity){
    fun getTrack(): TrackData{
        return activity.intent.getParcelableExtra<TrackData>(SEARCH_KEY)!!
    }
}