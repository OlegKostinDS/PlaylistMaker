package ru.dsvusial.playlistmaker.search.data.network

import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.data.network.model.NetworkResponse
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType

interface NetworkClient {
    fun search(
        query: String
    ) : NetworkResponse
}
