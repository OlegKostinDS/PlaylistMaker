package ru.dsvusial.playlistmaker.search.data.network.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
    @GET("/search?entity=song ")
    fun search(@Query("term") text: String) : Call<TrackResponse>
}