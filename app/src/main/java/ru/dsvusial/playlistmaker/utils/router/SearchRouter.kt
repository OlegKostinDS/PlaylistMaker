package ru.dsvusial.playlistmaker.utils.router

import android.app.Activity
import android.content.Intent
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.mediaPlayer.ui.MediaPlayerActivity
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY

class SearchRouter(val activity: Activity) {

    fun goBack() {
        activity.finish()
    }

    fun openMediaPlayerActivity(track: TrackData) {
        transitionToMediaPlayerActivity(track)
    }

    private fun transitionToMediaPlayerActivity(track: TrackData) {
        val sendIntent = Intent(activity, MediaPlayerActivity::class.java)
        sendIntent.putExtra(
            SEARCH_KEY, track
        )
        activity.startActivity(sendIntent)
    }
}