package ru.dsvusial.playlistmaker.music_library.domain.db

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface  FavoritesTrackRepository {

    suspend fun putTrackToFavorites(trackData: TrackData)

   suspend fun deleteTrackFromFavorites(trackData: TrackData)

    fun favoritesTracks(): Flow<List<TrackData>>
}