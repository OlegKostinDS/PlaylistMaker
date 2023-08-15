package ru.dsvusial.playlistmaker.detailedPlaylist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.dsvusial.playlistmaker.addPlaylist.data.db.PlaylistDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.TracksForPLaylitsDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.detailedPlaylist.domain.DetailedPlaylistRepository
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.data.converters.TrackDbConvertor

class DetailedPlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDataBase,
    private val tracksForPlaylistDatabase: TracksForPLaylitsDataBase,
    private val trackDbConvertor: TrackDbConvertor,
) : DetailedPlaylistRepository {
    override fun playlistsForTracks(listId: List<String>): Flow<List<TrackData>> {
        val tracks = tracksForPlaylistDatabase.tracksForPlaylistDao().getTracksForPlaylist()
            .map { convertFromTracksForPlaylistEntity(it) }
        return tracks.map { it.filter { listId.contains(it.trackId) } }

    }

    override fun getPlaylistForID(id: Int): Flow<PlaylistData> {
        return playlistDatabase.playlistDao().getPlaylistForId(id).map {
            trackDbConvertor.mapToPlaylistData(it)
        }
    }

    override suspend fun deleteTrack(track: TrackData) {

        tracksForPlaylistDatabase.tracksForPlaylistDao().deleteTrack(
            trackDbConvertor
                .mapTrackDataToTrackForPlaylistsEntity(track)
        )

    }

    private fun convertFromTracksForPlaylistEntity(tracks: List<TrackForPlaylistsEntity>): List<TrackData> {
        return tracks.map { track -> trackDbConvertor.mapFromTracksForPlaylist(track) }
    }

}
