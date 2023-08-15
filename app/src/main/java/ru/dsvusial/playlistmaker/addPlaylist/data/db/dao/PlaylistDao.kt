package ru.dsvusial.playlistmaker.addPlaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithoutFlow(): List<PlaylistEntity>

    @Query("SELECT playlistTracks FROM playlist_table")
    fun getPlaylistsIds(): Flow<List<String>>

    @Query("UPDATE playlist_table SET  playlistTracks =:tracks,playlistAmount =:amount WHERE id == :idPlaylist")
    fun updatePlaylist(tracks: String, amount: Int, idPlaylist: Int)



    @Update
    suspend fun updateList(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE id =:id")
    fun getPlaylistForId(id: Int): Flow<PlaylistEntity>


    @Query("SELECT * FROM playlist_table WHERE id =:id")
    suspend fun getPlaylistForIdWithoutFlow(id: Int): PlaylistEntity

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}