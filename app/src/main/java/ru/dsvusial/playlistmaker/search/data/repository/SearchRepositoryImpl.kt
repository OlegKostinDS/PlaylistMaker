package ru.dsvusial.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.data.network.NetworkClient
import ru.dsvusial.playlistmaker.search.data.network.model.TrackResponse
import ru.dsvusial.playlistmaker.search.domain.api.SearchRepository
import ru.dsvusial.playlistmaker.search.domain.model.SearchResult
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

class SearchRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val networkClient: NetworkClient,
    val gson: Gson
) : SearchRepository {


    override fun clear() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    override fun saveSearchHistory(historyTrack: ArrayList<TrackData>) {
        val json = gson.toJson(historyTrack)
        sharedPreferences.edit().putString(HISTORY_KEY, json).apply()
    }

    override fun loadTracks(
        query: String
    ): SearchResult {
        val response = networkClient.search(query)
        return when (response.code) {
            in 200..399 -> {
                val result = response as TrackResponse
                if (result.results.isEmpty()) {
                    SearchResult.Error(SearchUIType.NO_DATA)
                } else {
                    SearchResult.Success((response as TrackResponse).results.map {
                        TrackData(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            country = it.country,
                            primaryGenreName = it.primaryGenreName,
                            releaseDate = it.releaseDate,
                            previewUrl = it.previewUrl,
                        )
                    })
                }
            }

            in 400..499 -> SearchResult.Error(SearchUIType.NO_DATA)
            else -> SearchResult.Error(SearchUIType.NO_INTERNET)
        }
    }


    override fun getSearchHistory(): List<TrackData> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<TrackData>::class.java).toList()
    }

    companion object {
        const val HISTORY_KEY = "history_key"
    }

}