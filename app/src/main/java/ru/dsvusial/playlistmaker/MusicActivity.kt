package ru.dsvusial.playlistmaker

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class MusicActivity : AppCompatActivity() {
    lateinit var mpBackBtn: ImageButton
    lateinit var mpCover: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        initializeUI()
        initializeListeners()
    }


    private fun initializeUI() {
        mpBackBtn = findViewById(R.id.music_back_btn)
    }

    private fun initializeListeners() {
        mpBackBtn.setOnClickListener {
            onBackPressed()
        }
    }
}