package ru.dsvusial.playlistmaker.addPlaylist.domain

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface PlaylistRepository {
    suspend fun addPlaylist(playlistData: PlaylistData)
    suspend fun addTrackForPlaylists(trackData: TrackData)
    suspend fun updatePlaylist(playlistData: PlaylistData)
    fun playlists(): Flow<List<PlaylistData>>
}