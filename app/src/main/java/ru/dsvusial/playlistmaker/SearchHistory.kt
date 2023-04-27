package ru.dsvusial.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData


class SearchHistory(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val HISTORY_KEY = "history_key"
    }

    fun save(searchHistoryList: ArrayList<TrackData>) {
        val json = Gson().toJson(searchHistoryList)
        sharedPreferences.edit { putString(HISTORY_KEY, json) }

    }

    fun load(): Array<TrackData> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<TrackData>::class.java)
    }

    fun clear(){
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }
}