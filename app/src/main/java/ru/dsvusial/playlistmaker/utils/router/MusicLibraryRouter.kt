package ru.dsvusial.playlistmaker.utils.router

import android.app.Activity

class MusicLibraryRouter(val activity: Activity)  {
    fun goBack() {
        activity.finish()
    }

}