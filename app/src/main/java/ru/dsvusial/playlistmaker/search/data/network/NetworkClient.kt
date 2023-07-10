package ru.dsvusial.playlistmaker.search.data.network

import ru.dsvusial.playlistmaker.search.data.network.model.NetworkResponse

interface NetworkClient {
    suspend fun search(
        query: String
    ) : NetworkResponse
}
