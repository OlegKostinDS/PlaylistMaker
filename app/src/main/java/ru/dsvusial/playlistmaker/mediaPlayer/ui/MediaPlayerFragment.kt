package ru.dsvusial.playlistmaker.mediaPlayer.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.mediaPlayer.ui.view_model.MediaPlayerViewModel
import ru.dsvusial.playlistmaker.music_library.ui.model.PlaylistState
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY
import java.text.SimpleDateFormat
import java.util.Locale


class MediaPlayerFragment : Fragment() {

    private val viewModel by viewModel<MediaPlayerViewModel>()

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
    private lateinit var mpFavBtn: ImageButton
    private lateinit var mpAddPlaylistBtn: ImageButton
    private lateinit var mpCurrentTrackDuration: TextView
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetRecyclerView: RecyclerView
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var overlay: View
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var addNewPlaylistBtn: Button
    private lateinit var track: TrackData
    private var isContains: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUI(view)
        initAdapters()
        initBottomBehavior()
        track = requireArguments().getSerializable(SEARCH_KEY)!! as TrackData
        viewModel.preparePlayer(track.previewUrl)
        viewModel.isFavorite(track)
        getData(track)
        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) {
            when (it) {
                PlayStatus.OnPause -> mpPlayBtn.setImageResource(R.drawable.mp_play)
                PlayStatus.OnStart -> {
                    mpPlayBtn.setImageResource(R.drawable.mp_pause)
                }
            }
        }
        viewModel.getDurationLiveData().observe(viewLifecycleOwner) {
            setDuration(it)
        }
        viewModel.getFavoritesLiveData().observe(viewLifecycleOwner) {
            if (it) {
                mpFavBtn.setImageResource(R.drawable.mp_favorite_active)
            } else {
                mpFavBtn.setImageResource(R.drawable.mp_favorite)
            }
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistState.Empty -> {}
                is PlaylistState.Content -> {
                    bottomSheetAdapter.recentPlaylists.clear()
                    bottomSheetAdapter.recentPlaylists.addAll(state.playlists)
                    bottomSheetAdapter.notifyDataSetChanged()
                }

            }

        }
        viewModel.observeContainsPlaylist().observe(viewLifecycleOwner) {
            isContains = it
        }
        initializeListeners(track)

    }

    private fun initBottomBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
    }

    private fun initAdapters() {
        bottomSheetAdapter = BottomSheetAdapter { playlistData ->
            viewModel.isItInPlaylists(track, playlistData)
            if (isContains) {
                Toast.makeText(
                    context,
                    "Трек уже добавлен в плейлист ${playlistData.playlistName}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Toast.makeText(
                    context,
                    "Добавлено в плейлист ${playlistData.playlistName}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addTrackToPlaylistsId(playlistData, track)
                viewModel.addTracksForPlaylists(track)
            }

        }
        bottomSheetRecyclerView.adapter = bottomSheetAdapter
        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bottomSheetRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onViewPaused()
    }


    private fun initializeUI(view: View) {
        mpBackBtn = view.findViewById(R.id.music_back_btn)
        mpCover = view.findViewById(R.id.mp_cover)
        mpTrackName = view.findViewById(R.id.mp_trackName)
        mpArtistName = view.findViewById(R.id.mp_artistName)
        mpTrackDuration = view.findViewById(R.id.track_duration_value)
        mpTrackCountry = view.findViewById(R.id.track_country_value)
        mpTrackGenre = view.findViewById(R.id.track_primary_genre_name_value)
        mpTrackAlbum = view.findViewById(R.id.collection_duration_value)
        mpTrackAlbumText = view.findViewById(R.id.collection_duration_text)
        mpReleaseDate = view.findViewById(R.id.track_release_date_value)
        mpCurrentTrackDuration = view.findViewById(R.id.mp_current_track_duration)
        mpAddPlaylistBtn = view.findViewById(R.id.mp_add_btn)
        mpPlayBtn = view.findViewById(R.id.mp_play_btn)
        mpFavBtn = view.findViewById(R.id.mp_fav_btn)
        bottomSheetContainer = view.findViewById(R.id.playlists_bottom_sheet)
        bottomSheetRecyclerView = view.findViewById(R.id.bottom_sheet_recyclerview)
        overlay = view.findViewById(R.id.overlay)
        addNewPlaylistBtn = view.findViewById(R.id.add_new_playlist_btn)
        mpTrackAlbum.visibility = View.VISIBLE
        mpTrackAlbumText.visibility = View.VISIBLE

    }

    private fun initializeListeners(track: TrackData) {

        mpBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        mpPlayBtn.setOnClickListener {
            viewModel.onPlayBtnClicked(track.previewUrl)
        }
        mpFavBtn.setOnClickListener {
            viewModel.onFavBtnClicked(track)
        }
        mpAddPlaylistBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        addNewPlaylistBtn.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(
                R.id.action_mediaPlayerFragment_to_newPlaylistFragment
            )

        }
    }

    fun getData(trackData: TrackData) {
        val cornerRadius =
            requireActivity().resources.getDimensionPixelSize(R.dimen.main_btn_radius)
        Glide.with(this)
            .load(trackData.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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
        mpTrackAlbum.text = trackData.collectionName.ifEmpty {
            mpTrackAlbum.visibility = View.GONE
            mpTrackAlbumText.visibility = View.GONE
            ""
        }

        mpTrackGenre.text = trackData.primaryGenreName
        mpCurrentTrackDuration.text =
            getString(R.string.track_duration_zero_value)
        mpReleaseDate.text = trackData.releaseDate.substring(0, 4)


    }


    private fun setDuration(duration: String) {
        mpCurrentTrackDuration.text = duration
    }


}

