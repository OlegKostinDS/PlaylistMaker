package ru.dsvusial.playlistmaker.search.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import ru.dsvusial.playlistmaker.SearchHistory
import ru.dsvusial.playlistmaker.SearchHistory.Companion.HISTORY_KEY
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.data.network.NetworkClient
import ru.dsvusial.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

class SearchRepositoryImpl(private val sharedPreferences: SharedPreferences,
private val networkClient: NetworkClient) : SearchRepository {


    override fun clear() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    override fun saveSearchHistory(historyTrack: ArrayList<TrackData>) {
        val json = Gson().toJson(historyTrack)
        sharedPreferences.edit().putString(HISTORY_KEY, json).apply()
    }

    override fun loadTracks(
        query: String,
        onSuccess: (list: List<TrackData>) -> Unit,
        onError: (error: SearchUIType) -> Unit
    ) {
        networkClient.search(query,onSuccess,onError)
    }

    override fun getSearchHistory(): List<TrackData> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<TrackData>::class.java).toList()
    }


}