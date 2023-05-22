package ru.dsvusial.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.dsvusial.playlistmaker.di.dataModule
import ru.dsvusial.playlistmaker.di.interactorModule
import ru.dsvusial.playlistmaker.di.repositoryModule
import ru.dsvusial.playlistmaker.di.viewModelModule
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository
import ru.dsvusial.playlistmaker.utils.THEME_KEY






class App : Application() {
    var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }



       switchTheme(getKoin().get<SettingsRepository>().getTheme(THEME_KEY))
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