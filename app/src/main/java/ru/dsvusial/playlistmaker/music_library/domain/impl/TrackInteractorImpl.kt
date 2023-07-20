package ru.dsvusial.playlistmaker.music_library.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.domain.db.FavoritesTrackRepository
import ru.dsvusial.playlistmaker.music_library.domain.db.TrackInteractor

class TrackInteractorImpl(val trackRepository: FavoritesTrackRepository) : TrackInteractor {


    override fun getFavoritesTracks(): Flow<List<TrackData>> {
        return trackRepository.favoritesTracks()
    }

    override suspend fun putFavoriteTrack(trackData: TrackData) {
        trackRepository.putTrackToFavorites(trackData)
    }

    override suspend fun unputFavoriteTrack(trackData: TrackData) {
        trackRepository.deleteTrackFromFavorites(trackData)
    }
}