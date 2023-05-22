package ru.dsvusial.playlistmaker.main.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.main.ui.model.MainNavState
import ru.dsvusial.playlistmaker.main.ui.view_model.MainViewModel
import ru.dsvusial.playlistmaker.music_library.MusicActivity
import ru.dsvusial.playlistmaker.mediaPlayer.ui.SearchActivity
import ru.dsvusial.playlistmaker.settings.ui.SettingsActivity


class MainActivity : AppCompatActivity() {

    val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val musicBtn = findViewById<Button>(R.id.music_btn)
        val settingBtn = findViewById<Button>(R.id.setting_btn)
        viewModel.getMainStatusLiveData().observe(this) {
            when (it) {
                MainNavState.SEARCH -> startActivity(
                    Intent(
                        this@MainActivity,
                        SearchActivity::class.java
                    )
                )
                MainNavState.MUSIC_LIBLARY -> startActivity(
                    Intent(
                        this@MainActivity,
                        MusicActivity::class.java
                    )
                )
                MainNavState.SETTINGS -> startActivity(
                    Intent(
                        this@MainActivity,
                        SettingsActivity::class.java
                    )
                )
            }
        }


        searchBtn.setOnClickListener {
            viewModel.startSearchActivity()
        }

        musicBtn.setOnClickListener {
            viewModel.startMusicLiblaryActivity()

        }

        settingBtn.setOnClickListener {
            viewModel.startSettingsActivity()

        }
    }

}