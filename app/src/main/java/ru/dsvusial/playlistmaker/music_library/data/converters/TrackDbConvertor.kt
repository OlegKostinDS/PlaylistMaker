package ru.dsvusial.playlistmaker.music_library.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.data.db.entity.TrackEntity

class TrackDbConvertor(val gson: Gson) {
    fun mapToPlaylistEntity(playlist: PlaylistData): PlaylistEntity {
        return PlaylistEntity(
            id = 0,
            playlistName = playlist.playlistName,
            playlistDesc = playlist.playlistDesc,
            playlistUri = playlist.playlistUri,
            playlistTracks = gson.toJson(playlist.playlistTracks),
            playlistAmount = playlist.playlistAmount,
        )
    }
    fun mapListToString(list : List<String>): String{
      return  gson.toJson(list)
    }

    fun mapToPlaylistData(playlist: PlaylistEntity): PlaylistData{
        val arrayTutorialType = object : TypeToken<List<String>>() {}.type
        return PlaylistData(
            id = playlist.id,
            playlistName = playlist.playlistName,
            playlistDesc = playlist.playlistDesc,
            playlistUri = playlist.playlistUri,

            playlistTracks = gson.fromJson(playlist.playlistTracks,arrayTutorialType),
            playlistAmount = playlist.playlistAmount,

        )
    }

    fun map(track: TrackEntity): TrackData {
        return TrackData(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
        )
    }

    fun map(track: TrackData): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
        )
    }
    fun mapTrackDataToTrackForPlaylistsEntity(track: TrackData): TrackForPlaylistsEntity {
        return TrackForPlaylistsEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
        )
    }

}