package ru.dsvusial.playlistmaker.addPlaylist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.TracksForPLaylitsDataBase.Companion.dbVersionForTFPDB
import ru.dsvusial.playlistmaker.addPlaylist.data.db.dao.TracksForPlaylistDao
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.TrackForPlaylistsEntity

@Database(version = dbVersionForTFPDB, entities = [TrackForPlaylistsEntity::class])
abstract class TracksForPLaylitsDataBase : RoomDatabase() {
    abstract fun tracksForPlaylistDao(): TracksForPlaylistDao
    companion object{
        const val dbVersionForTFPDB = 20
    }
}