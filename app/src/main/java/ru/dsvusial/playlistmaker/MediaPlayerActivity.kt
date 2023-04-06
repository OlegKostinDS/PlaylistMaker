package ru.dsvusial.playlistmaker


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.network.TrackData
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MP_DELAY = 1000L
    }

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
    private lateinit var mpPlayBtn: ImageButton
    private lateinit var mpCurrentTrackDuration: TextView
    private lateinit var track: TrackData
    private lateinit var url: String
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        initializeUI()
        getData()
        preparePlayer()
        initializeListeners()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
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
        mpCurrentTrackDuration = findViewById(R.id.mp_current_track_duration)
        mpPlayBtn = findViewById(R.id.mp_play_btn)
        mpTrackAlbum.visibility = View.VISIBLE
        mpTrackAlbumText.visibility = View.VISIBLE
        mpPlayBtn.isEnabled = false
    }

    private fun initializeListeners() {
        mpBackBtn.setOnClickListener {
            onBackPressed()
        }
        mpPlayBtn.setOnClickListener {
            playbackControl()
        }
    }

    private fun getData() {

        val cornerRadius =
            applicationContext.resources.getDimensionPixelSize(R.dimen.main_btn_radius)
        track = intent.getParcelableExtra<TrackData>(SEARCH_KEY) ?: return
        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.nodata)
            .centerInside()
            .transform(RoundedCorners(cornerRadius))
            .into(mpCover)
        mpTrackName.text = track.trackName
        mpArtistName.text = track.artistName
        mpTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                .toString()
        mpTrackCountry.text = track.country
        mpTrackAlbum.text = if (track.collectionName.isNullOrEmpty()) {
            mpTrackAlbum.visibility = View.GONE
            mpTrackAlbumText.visibility = View.GONE
            ""
        } else track.collectionName
        url = track.previewUrl
        mpTrackGenre.text = track.primaryGenreName
        mpCurrentTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        mpReleaseDate.text = track.releaseDate?.substring(0, 4)
    }

    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                mpPlayBtn.isEnabled = true
                mpPlayBtn.setImageResource(R.drawable.mp_play)
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                mpPlayBtn.setImageResource(R.drawable.mp_play)
                playerState = STATE_PREPARED
                setDuration(0)
                handler.removeCallbacksAndMessages(null)
            }
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mpPlayBtn.setImageResource(R.drawable.mp_pause)
        handler.postDelayed(object : Runnable {
            override fun run() {
                setDuration(mediaPlayer.currentPosition)
                handler.postDelayed(this, MP_DELAY)
            }
        }, MP_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        mpPlayBtn.setImageResource(R.drawable.mp_play)
        handler.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun setDuration(milliseconds: Int) {
        mpCurrentTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }
}