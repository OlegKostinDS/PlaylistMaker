package ru.dsvusial.playlistmaker.addPlaylist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.dsvusial.playlistmaker.addPlaylist.data.db.PlaylistDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.TracksForPLaylitsDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity
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
        appTracksDatabase.tracksForPlaylistDao()
            .insertTrackForPlaylists(
                trackDbConvertor.mapTrackDataToTrackForPlaylistsEntity(
                    trackData
                )
            )
    }

    override suspend fun updatePlaylist(playlistData: PlaylistData) {
        appDatabase.playlistDao()
            .updatePlaylist(
                tracks = trackDbConvertor.mapListToString(playlistData.playlistTracks),
                amount = playlistData.playlistAmount,
                idPlaylist = playlistData.id
            )
    }

    override fun playlists(): Flow<List<PlaylistData>> {
        return appDatabase.playlistDao().getPlaylists().map { convertFromPlaylistEntity(it) }
    }


    override suspend fun deleteTrackFromPlaylistById(
        trackId: String,
        playlistId: Int
    ): Flow<List<TrackData>> {
        lateinit var tempPlaylistData: PlaylistData

        val playlist = trackDbConvertor.mapToPlaylistData(
            appDatabase.playlistDao().getPlaylistForIdWithoutFlow(playlistId)
        )
        tempPlaylistData = playlist.copy(playlistTracks = playlist.playlistTracks.filterNot {
            it.contains(trackId)
        })
        tempPlaylistData =
            tempPlaylistData.copy(playlistAmount = tempPlaylistData.playlistTracks.size)
        appDatabase.playlistDao().updateList(trackDbConvertor.mapToPlaylistEntity(tempPlaylistData))


        deleteUniqueTrackFromTracksForPlaylistDB(trackId, playlistId)


        val flowCurrentTracks = appTracksDatabase.tracksForPlaylistDao().getTracksForPlaylist()
            .map { convertFromTracksForPlaylistEntity(it) }

        return flowCurrentTracks.map { currentTracks ->
            currentTracks.filter { trackData ->
                tempPlaylistData.playlistTracks.contains(
                    trackData.trackId
                )
            }
        }
    }

    override suspend fun deletePlaylist(playlistData: PlaylistData) {

        val currentTracksForPlaylist = convertFromTracksForPlaylistEntity(
            appTracksDatabase.tracksForPlaylistDao().getTracksForPlaylistWithoutFlow()
        )
        currentTracksForPlaylist.forEach { playlist ->
            deleteUniqueTrackFromTracksForPlaylistDB(playlist.trackId, playlistData.id)
        }

        appDatabase.playlistDao().deletePlaylist(trackDbConvertor.mapToPlaylistEntity(playlistData))
    }

    override suspend fun updatePlaylistByEdit(playlistData: PlaylistData) {
        appDatabase.playlistDao().updateList(trackDbConvertor.mapToPlaylistEntity(playlistData))
    }

    private suspend fun deleteUniqueTrackFromTracksForPlaylistDB(trackId: String, playlistId: Int) {
        var shouldDelete = true
        val tempPlaylistDataList: List<PlaylistData> =
            appDatabase.playlistDao().getPlaylistsWithoutFlow()
                .map { trackDbConvertor.mapToPlaylistData(it) }
        tempPlaylistDataList.forEach {
            if (it.playlistTracks.contains(trackId) && it.id != playlistId
            ) {
                shouldDelete = false
            }
        }
        if (shouldDelete) {

            appTracksDatabase.tracksForPlaylistDao().deleteTrackById(trackId)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistData> {
        return playlists.map { playlist -> trackDbConvertor.mapToPlaylistData(playlist) }
    }

    private fun convertFromTracksForPlaylistEntity(tracks: List<TrackForPlaylistsEntity>): List<TrackData> {
        return tracks.map { track -> trackDbConvertor.mapFromTracksForPlaylist(track) }
    }
}