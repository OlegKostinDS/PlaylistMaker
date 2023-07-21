package ru.dsvusial.playlistmaker.music_library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.data.converters.TrackDbConvertor
import ru.dsvusial.playlistmaker.music_library.data.db.AppDatabase
import ru.dsvusial.playlistmaker.music_library.data.db.entity.TrackEntity
import ru.dsvusial.playlistmaker.music_library.domain.db.FavoritesTrackRepository

class FavoritesTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesTrackRepository {
    override suspend fun putTrackToFavorites(trackData: TrackData) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(trackData))
    }

    override suspend fun deleteTrackFromFavorites(trackData: TrackData) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(trackData))
    }

    override fun favoritesTracks(): Flow<List<TrackData>> {

        return appDatabase.trackDao().getTracks().map {
            convertFromTrackEntity(it)
        }
    }

    override fun favoritesIds(track: String): Flow<Boolean> = flow {
        emit((appDatabase.trackDao().getTrackIds()).contains(track))
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackData> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}