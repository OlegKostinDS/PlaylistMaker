package ru.dsvusial.playlistmaker.detailedPlaylist.domain

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface DetailedPlaylistRepository {

    fun playlistsForTracks(tracksId : List<String>): Flow<List<TrackData>>

    fun getPlaylistForID(id: Int) : Flow<PlaylistData>
   suspend  fun deleteTrack(track: TrackData)
}