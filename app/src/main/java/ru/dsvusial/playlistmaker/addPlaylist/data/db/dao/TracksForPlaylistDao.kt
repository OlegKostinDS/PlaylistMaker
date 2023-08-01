package ru.dsvusial.playlistmaker.addPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity

@Dao
interface TracksForPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackForPlaylists(track: TrackForPlaylistsEntity)
}