package ru.dsvusial.playlistmaker.music_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dsvusial.playlistmaker.music_library.data.db.AppDatabase.Companion.dbVersionAppDataBase
import ru.dsvusial.playlistmaker.music_library.data.db.dao.TrackDao
import ru.dsvusial.playlistmaker.music_library.data.db.entity.TrackEntity

@Database(version = dbVersionAppDataBase, entities = [TrackEntity::class])
abstract  class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
    companion object{
        const val dbVersionAppDataBase = 1
    }
}