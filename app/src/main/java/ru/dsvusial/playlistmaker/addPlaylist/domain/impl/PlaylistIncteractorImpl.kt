package ru.dsvusial.playlistmaker.addPlaylist.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistIncteractor
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistRepository
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

class PlaylistIncteractorImpl(val playlistRepository: PlaylistRepository) : PlaylistIncteractor {
    override suspend fun addPlaylist(playlistData: PlaylistData) {
        playlistRepository.addPlaylist(playlistData)
    }

    override suspend fun addTrackForPlaylists(trackData: TrackData) {
        playlistRepository.addTrackForPlaylists(trackData)
    }

    override suspend fun updatePlaylist(playlistData: PlaylistData) {
        playlistRepository.updatePlaylist(playlistData)
    }

    override fun getPlaylists(): Flow<List<PlaylistData>> {
        return playlistRepository.playlists()
    }
}