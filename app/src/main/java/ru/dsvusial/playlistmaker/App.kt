package ru.dsvusial.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val THEME_KEY = "theme_key"

object ThemePreferences {

}

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val settingsTheme = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        darkTheme = settingsTheme.getBoolean(THEME_KEY, false)
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}