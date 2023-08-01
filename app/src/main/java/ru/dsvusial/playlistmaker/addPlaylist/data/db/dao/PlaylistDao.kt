package ru.dsvusial.playlistmaker.addPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlist_table SET  playlistTracks =:tracks,playlistAmount =:amount WHERE id == :idPlaylist")
    fun updatePlaylist(tracks: String, amount: Int, idPlaylist: Int)

}