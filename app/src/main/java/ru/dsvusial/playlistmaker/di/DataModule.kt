package ru.dsvusial.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dsvusial.playlistmaker.addPlaylist.data.db.PlaylistDataBase
import ru.dsvusial.playlistmaker.addPlaylist.data.db.TracksForPLaylitsDataBase
import ru.dsvusial.playlistmaker.music_library.data.converters.TrackDbConvertor
import ru.dsvusial.playlistmaker.music_library.data.db.AppDatabase
import ru.dsvusial.playlistmaker.search.data.network.NetworkClient
import ru.dsvusial.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.dsvusial.playlistmaker.search.data.network.model.TrackApi

const val APP_PREFERENCES = "app_preferences"
private val baseUrl = "https://itunes.apple.com"
val dataModule = module {
    single<TrackApi> {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()

        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TrackApi::class.java)
    }
    single {
        androidContext()
            .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    }
    single<NetworkClient> {
        RetrofitNetworkClient(get(),androidContext())
    }
    factory { Gson() }

    single {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single {
        Room
            .databaseBuilder(androidContext(), TracksForPLaylitsDataBase::class.java, "tracks_for_playlist_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        Room
            .databaseBuilder(androidContext(), PlaylistDataBase::class.java, "playlist_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory { TrackDbConvertor(get()) }

}