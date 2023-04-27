package ru.dsvusial.playlistmaker.mediaPlayer.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.creator.Creator
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerPresenterImpl
import ru.dsvusial.playlistmaker.mediaPlayer.presentation.MediaPlayerView
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerActivity : AppCompatActivity(), MediaPlayerView {

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
    lateinit var presenter: MediaPlayerPresenterImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        initializeUI()

        presenter = Creator.providePresenter(
            mediaPlayerView = this,
            activity = this
        )
        initializeListeners()

    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPaused()

    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onViewDestroyed()
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

    }

    private fun initializeListeners() {
        mpBackBtn.setOnClickListener {
            presenter.onBackPressed()
        }
        mpPlayBtn.setOnClickListener {
            presenter.onPlayBtnClicked()
        }
    }

    override fun getData(trackData: TrackData) {

        val cornerRadius =
            applicationContext.resources.getDimensionPixelSize(R.dimen.main_btn_radius)

        Glide.with(this)
            .load(trackData?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.nodata)
            .centerInside()
            .transform(RoundedCorners(cornerRadius))
            .into(mpCover)
        mpTrackName.text = trackData.trackName
        mpArtistName.text = trackData.artistName
        mpTrackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackData.trackTimeMillis)
                .toString()
        mpTrackCountry.text = trackData.country
        mpTrackAlbum.text = if (trackData.collectionName.isNullOrEmpty()) {
            mpTrackAlbum.visibility = View.GONE
            mpTrackAlbumText.visibility = View.GONE
            ""
        } else trackData.collectionName

        mpTrackGenre.text = trackData.primaryGenreName
        mpCurrentTrackDuration.text =
            "00:00"
        mpReleaseDate.text = trackData.releaseDate?.substring(0, 4)
    }


    override fun setStartImage() {

        mpPlayBtn.setImageResource(R.drawable.mp_play)
    }

    override fun goBack() {
        finish()
    }


    override fun setPausedImage() {

        mpPlayBtn.setImageResource(R.drawable.mp_pause)

    }


    override fun setDuration(duration: String) {
        mpCurrentTrackDuration.text = duration
    }


}

