package ru.dsvusial.playlistmaker.detailedPlaylist.domain

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface DetailedPlaylistInteractor {

    fun getTracksForPlaylist(tracks: List<String>): Flow<List<TrackData>>

    fun getPlaylistForId(id: Int): Flow<PlaylistData>
    suspend fun deleteTrack(track: TrackData)
}