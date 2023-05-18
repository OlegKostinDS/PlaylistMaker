package ru.dsvusial.playlistmaker.settings.data

import android.content.SharedPreferences
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl (val sharedPreferences: SharedPreferences): SettingsRepository {


    override fun putSharedTheme(key: String, status: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, status).apply()
    }

    override fun getTheme(key: String): Boolean {
    return    sharedPreferences.getBoolean(
            key,
            false)
    }
}