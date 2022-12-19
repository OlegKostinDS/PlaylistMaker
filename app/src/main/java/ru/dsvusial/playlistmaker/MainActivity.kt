package ru.dsvusial.playlistmaker


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val musicBtn = findViewById<Button>(R.id.music_btn)
        val settingBtn = findViewById<Button>(R.id.setting_btn)
        val searchBtnClickListener: View.OnClickListener =
            object : View.OnClickListener {
                override fun onClick(p0: View?) {

                }
            }
        searchBtn.setOnClickListener(searchBtnClickListener)

        musicBtn.setOnClickListener{

        }
        settingBtn.setOnClickListener{

        }
    }

}