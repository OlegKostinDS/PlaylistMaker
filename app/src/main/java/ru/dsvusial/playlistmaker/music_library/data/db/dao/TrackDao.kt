package ru.dsvusial.playlistmaker.music_library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.music_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorites_track_table")
     fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorites_track_table")
    suspend fun getTrackIds(): List<String>
}