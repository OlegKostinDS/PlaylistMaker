package ru.dsvusial.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.network.TrackData
import java.text.SimpleDateFormat
import java.util.*

class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var mpBackBtn: ImageButton
    private lateinit var mpCover: ImageView
    private lateinit var mpTrackName: TextView
    private lateinit var mpArtistName: TextView
    private lateinit var mpTrackDuration: TextView
    private lateinit var mpTrackCountry: TextView
    private lateinit var mpTrackGenre: TextView
    private lateinit var mpTrackAlbum: TextView
    private lateinit var mpTrackAlbumText: TextView
    private lateinit var mpReleaseDate: TextView

    private lateinit var track: TrackData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        initializeUI()
        initializeListeners()
        getData()

    }


    private fun initializeUI() {
        mpBackBtn = findViewById(R.id.music_back_btn)
        mpCover = findViewById(R.id.mp_cover)
        mpTrackName = findViewById(R.id.mp_trackName)
        mpArtistName = findViewById(R.id.mp_artistName)
        mpTrackDuration = findViewById(R.id.track_duration_value)
        mpTrackCountry = findViewById(R.id.track_country_value)
        mpTrackGenre = findViewById(R.id.track_primary_genre_name_value)
        mpTrackAlbum = findViewById(R.id.collection_duration_value)
        mpTrackAlbumText = findViewById(R.id.collection_duration_text)
        mpReleaseDate = findViewById(R.id.track_release_date_value)
        mpTrackAlbum.visibility = View.VISIBLE
        mpTrackAlbumText.visibility = View.VISIBLE
    }

    private fun initializeListeners() {
        mpBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {

        val cornerRadius =
            applicationContext.resources.getDimensionPixelSize(R.dimen.main_btn_radius)
        track = intent.getParcelableExtra<TrackData>(SEARCH_KEY)!!
        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.nodata)
            .centerInside()
            .transform(RoundedCorners(cornerRadius))

            .into(mpCover)
        mpTrackName.text = track?.trackName
        mpArtistName.text = track?.artistName
        mpTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
                .toString()
        mpTrackCountry.text = track?.country
        mpTrackAlbum.text = if (track?.collectionName.isNullOrEmpty()) {
            mpTrackAlbum.visibility = View.GONE
            mpTrackAlbumText.visibility = View.GONE
            ""
        } else track?.collectionName

        mpTrackGenre.text = track?.primaryGenreName
        mpReleaseDate.text = track?.releaseDate?.substring(0, 4)
    }
}