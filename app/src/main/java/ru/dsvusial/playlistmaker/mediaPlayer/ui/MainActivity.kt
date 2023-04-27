package ru.dsvusial.playlistmaker.mediaPlayer.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.dsvusial.playlistmaker.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val musicBtn = findViewById<Button>(R.id.music_btn)
        val settingBtn = findViewById<Button>(R.id.setting_btn)

        searchBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        musicBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MusicActivity::class.java))
        }

        settingBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }

}