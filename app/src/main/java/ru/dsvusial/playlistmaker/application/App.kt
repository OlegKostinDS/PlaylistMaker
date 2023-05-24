package ru.dsvusial.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import ru.dsvusial.playlistmaker.di.dataModule
import ru.dsvusial.playlistmaker.di.interactorModule
import ru.dsvusial.playlistmaker.di.repositoryModule
import ru.dsvusial.playlistmaker.di.viewModelModule
import ru.dsvusial.playlistmaker.settings.domain.api.SettingsRepository
import ru.dsvusial.playlistmaker.utils.THEME_KEY


class App : Application() {
    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
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