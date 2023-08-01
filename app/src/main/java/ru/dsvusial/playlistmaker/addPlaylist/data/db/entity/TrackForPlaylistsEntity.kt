package ru.dsvusial.playlistmaker.addPlaylist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_for_playlist_table")
data class TrackForPlaylistsEntity (
    @PrimaryKey(autoGenerate = false)
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)