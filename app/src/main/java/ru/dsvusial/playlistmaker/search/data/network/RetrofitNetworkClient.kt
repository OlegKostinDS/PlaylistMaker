package ru.dsvusial.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.dsvusial.playlistmaker.search.data.network.model.NetworkResponse
import ru.dsvusial.playlistmaker.search.data.network.model.TrackApi

class RetrofitNetworkClient(
    private val songService: TrackApi,
    private val context: Context
) : NetworkClient {

    override suspend fun search(
        query: String
    ): NetworkResponse {
        if (!isConnected()) return NetworkResponse().apply { code = -1 }
        val response =
            songService.search(query)
        return response.body()?.apply { code = response.code() }
            ?: NetworkResponse().apply { code = response.code() }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}

