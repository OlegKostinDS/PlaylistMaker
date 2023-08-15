package ru.dsvusial.playlistmaker.detailedPlaylist.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.detailedPlaylist.domain.DetailedPlaylistInteractor
import ru.dsvusial.playlistmaker.detailedPlaylist.domain.DetailedPlaylistRepository
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

class DetailedPlaylistInteractorImpl(val detaliedPlaylistRepository: DetailedPlaylistRepository) :DetailedPlaylistInteractor {
    override fun getTracksForPlaylist(tracks: List<String>): Flow<List<TrackData>> {
    return detaliedPlaylistRepository.playlistsForTracks(tracks)
    }

    override fun getPlaylistForId(id: Int): Flow<PlaylistData> {
     return   detaliedPlaylistRepository.getPlaylistForID(id)
    }

    override suspend fun deleteTrack(track: TrackData) {
        detaliedPlaylistRepository.deleteTrack(track)
    }

}