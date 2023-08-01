package ru.dsvusial.playlistmaker.music_library.domain.db

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData

interface TrackInteractor {
    suspend  fun putFavoriteTrack(trackData: TrackData)
    fun getFavoritesTracks(): Flow<List<TrackData>>
    suspend fun  unputFavoriteTrack(trackData: TrackData)

   fun getFavoriteIds(trackId: String): Flow<Boolean>
}