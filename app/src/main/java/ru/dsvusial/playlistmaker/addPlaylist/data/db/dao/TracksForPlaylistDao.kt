package ru.dsvusial.playlistmaker.addPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity

@Dao
interface TracksForPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackForPlaylists(track: TrackForPlaylistsEntity)

    @Query("SELECT * FROM track_for_playlist_table")
    fun getTracksForPlaylist(): Flow<List<TrackForPlaylistsEntity>>
    @Query("SELECT * FROM track_for_playlist_table")
    fun getTracksForPlaylistWithoutFlow(): List<TrackForPlaylistsEntity>
    @Delete
    suspend fun deleteTrack(track: TrackForPlaylistsEntity)

    @Query("DELETE FROM track_for_playlist_table WHERE trackId =:trackId")
            suspend fun deleteTrackById(trackId : String)
}