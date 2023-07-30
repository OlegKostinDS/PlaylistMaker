package ru.dsvusial.playlistmaker.addPlaylist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.dsvusial.playlistmaker.addPlaylist.data.db.PlaylistDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.TracksForPLaylitsDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity
import ru.dsvusial.playlistmaker.addPlaylist.domain.PlaylistRepository
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.data.converters.TrackDbConvertor

class PlaylistRepositoryImpl(
    private val appDatabase: PlaylistDataBase,
    private val appTracksDatabase: TracksForPLaylitsDataBase,
    private val trackDbConvertor: TrackDbConvertor,
) : PlaylistRepository {
    override suspend fun addPlaylist(playlistData: PlaylistData) {
        appDatabase.playlistDao().insertPlaylist(trackDbConvertor.mapToPlaylistEntity(playlistData))
    }

    override suspend fun addTrackForPlaylists(trackData: TrackData) {
        appTracksDatabase.tracksForPaylistDao()
            .insertTrackForPlaylists(trackDbConvertor.mapTrackDataToTrackForPlaylistsEntity(trackData))
    }

    override suspend fun updatePlaylist(playlistData: PlaylistData) {
        appDatabase.playlistDao()
            .updatePlaylist(trackDbConvertor.mapListToString(playlistData.playlistTracks), playlistData.playlistAmount
            , playlistData.id)
    }

    override fun playlists(): Flow<List<PlaylistData>> {
       return appDatabase.playlistDao().getPlaylists().map { convertFromPlaylistEntity(it) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistData> {
        return playlists.map { playlist -> trackDbConvertor.mapToPlaylistData(playlist) }
    }

}