package ru.dsvusial.playlistmaker.addPlaylist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.dao.PlaylistDao
import ru.dsvusial.playlistmaker.addPlaylist.data.db.entity.PlaylistEntity

@Database(version = 6, entities = [PlaylistEntity::class])
abstract class PlaylistDataBase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}


