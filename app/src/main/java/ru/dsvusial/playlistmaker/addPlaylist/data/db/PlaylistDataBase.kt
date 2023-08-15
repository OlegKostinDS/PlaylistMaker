package ru.dsvusial.playlistmaker.addPlaylist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.PlaylistDataBase.Companion.dbVersionPlaylistDB
import ru.dsvusial.playlistmaker.addPlaylist.data.db.dao.PlaylistDao
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity

@Database(version = dbVersionPlaylistDB, entities = [PlaylistEntity::class])
abstract class PlaylistDataBase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    companion object{
        const val dbVersionPlaylistDB = 21
    }
}


