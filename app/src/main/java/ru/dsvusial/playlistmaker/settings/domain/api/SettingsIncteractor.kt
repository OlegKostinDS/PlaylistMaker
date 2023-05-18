package ru.dsvusial.playlistmaker.settings.domain.impl

interface SettingsInteractor {
    fun saveTheme(key: String, status: Boolean)
     fun getTheme(key: String): Boolean
}