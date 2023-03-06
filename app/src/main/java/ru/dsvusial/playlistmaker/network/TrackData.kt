package ru.dsvusial.playlistmaker.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackData (
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
): Parcelable