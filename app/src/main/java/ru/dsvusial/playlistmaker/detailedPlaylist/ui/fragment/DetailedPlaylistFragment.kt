package ru.dsvusial.playlistmaker.detailedPlaylist.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.detailedPlaylist.ui.viewmodel.DetailedPlaylistViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.ui.PLAYLIST_KEY
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY
import ru.dsvusial.playlistmaker.search.ui.TrackAdapter
import ru.dsvusial.playlistmaker.utils.ConvertUtil
import ru.dsvusial.playlistmaker.utils.DateTimeUtil


const val EDIT_KEY = "edit_key"

class DetailedPlaylistFragment : Fragment() {
    val viewModel by viewModel<DetailedPlaylistViewModel>()
    private lateinit var playlistData: PlaylistData
    private lateinit var currentTracks: List<TrackData>
    private lateinit var posterForDetailedPlaylistFragment: ImageView
    private lateinit var shareDetailedPlaylist: ImageView
    private lateinit var moreAboutDetailedPlaylist: ImageView
    private lateinit var posterName: TextView
    private lateinit var posterDesc: TextView
    private lateinit var tracksDuration: TextView
    private lateinit var trackAmount: TextView
    private lateinit var detailedBottomSheetContainer: LinearLayout
    private lateinit var moreDetailedBottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var moreBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var overlay: View
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var detailedBottomSheetRecycler: RecyclerView
    private lateinit var backBtn: MaterialToolbar
    private lateinit var deleteDialog: MaterialAlertDialogBuilder
    private lateinit var infoBottomImage: ImageView
    private lateinit var infoBottomName: TextView
    private lateinit var infoBottomAmount: TextView
    private lateinit var shareInfoBottomSheet: TextView
    private lateinit var editInfoBottomSheet: TextView
    private lateinit var deleteInfoBottomSheet: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistData = requireArguments().getSerializable(PLAYLIST_KEY)!! as PlaylistData
        viewModel.fillData(playlistData)
        initUI(view)
        initBottomBehavior()

        initListeners(playlistData)

        viewModel.getTracks().observe(viewLifecycleOwner) { tracks ->
            currentTracks = tracks

            tracksDuration.text =
                ConvertUtil.conventAmountToMinutesString(
                    DateTimeUtil.formatTimeMillisToMinutesString(
                        tracks.sumOf { it.trackTimeMillis })
                )

            trackAdapter = TrackAdapter()
            trackAdapter.onItemClick = {
                findNavController().navigate(
                    R.id.action_detailedPlaylistFragment_to_mediaPlayerFragment,
                    bundleOf(SEARCH_KEY to it)
                )
            }

            trackAdapter.onItemLongClick = { track ->
                deleteDialog = MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Хотите удалить трек?")
                    .setNegativeButton(R.string.No) { dialog, which ->
                    }
                    .setPositiveButton(R.string.Yes) { dialog, which ->
                        viewModel.deleteTrackById(track.trackId, playlistData.id)
                    }
                deleteDialog.show()
            }
            trackAdapter.recentTracks.clear()
            trackAdapter.recentTracks.addAll(tracks)

            detailedBottomSheetRecycler.adapter = trackAdapter
            detailedBottomSheetRecycler.layoutManager = LinearLayoutManager(requireContext())
            trackAdapter.notifyDataSetChanged()
        }

        viewModel.getPlaylist().observe(viewLifecycleOwner) { playlist ->
            trackAmount.text = ConvertUtil.conventAmountToTrackString(playlist.playlistAmount)
            trackAdapter = TrackAdapter()
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun initListeners(playlistData: PlaylistData) {
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        shareDetailedPlaylist.setOnClickListener {
            sharePlaylist()
        }
        shareInfoBottomSheet.setOnClickListener {
            sharePlaylist()
        }
        moreAboutDetailedPlaylist.setOnClickListener {
            val cornerRadius =
                requireActivity().resources.getDimensionPixelSize(R.dimen.lesser_btn_radius)
            Glide.with(requireActivity())
                .load(playlistData.playlistUri)
                .placeholder(R.drawable.nodata)
                .transform(CenterCrop(),RoundedCorners(cornerRadius))
                .into(infoBottomImage)
            infoBottomName.text = playlistData.playlistName
            infoBottomAmount.text =
                ConvertUtil.conventAmountToTrackString(playlistData.playlistAmount)
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        editInfoBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailedPlaylistFragment_to_editPlaylistFragment,
                createArgs(playlistData)
            )

        }
        deleteInfoBottomSheet.setOnClickListener {
            deleteDialog = MaterialAlertDialogBuilder(requireContext())
                .setMessage("Хотите удалить плейлист «${playlistData.playlistName}»?")
                .setNegativeButton(getString(R.string.No)) { dialog, which ->
                }
                .setPositiveButton(getString(R.string.Yes)) { dialog, which ->
                    viewModel.deletePlaylist(playlistData)
                    findNavController().popBackStack()
                }
            deleteDialog.show()

        }


    }

    private fun sharePlaylist() {
        if (playlistData.playlistTracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    getSharingTextFromPlaylist(playlistData, currentTracks)
                )
                type = "text/plain"
                requireActivity().startActivity(this)
            }
        }
    }

    private fun getSharingTextFromPlaylist(
        playlistData: PlaylistData,
        currentTracks: List<TrackData>
    ): String {
        val tempString = mutableListOf<String>()
        tempString.add(playlistData.playlistName)
        if (!playlistData.playlistDesc.isNullOrEmpty()) {
            tempString.add(playlistData.playlistDesc)
        }
        tempString.add(
            ConvertUtil.conventAmountToTrackString(playlistData.playlistAmount)
        )
        currentTracks.forEachIndexed { index, track ->
            tempString.add(
                "${index + 1} ${track.artistName} - ${track.trackName} ${
                    DateTimeUtil.formatTimeMillisToString(track.trackTimeMillis)
                }"
            )
        }
        return tempString.joinToString("\n")

    }

    private fun initUI(view: View) {
        backBtn = view.findViewById<MaterialToolbar>(R.id.detailed_back_btn)
        posterName = view.findViewById(R.id.detailed_playlist_name)
        posterDesc = view.findViewById(R.id.detailed_playlist_description)
        trackAmount = view.findViewById(R.id.tracks_amount_detailed)
        shareDetailedPlaylist = view.findViewById(R.id.share_detailed_playlist)
        moreAboutDetailedPlaylist = view.findViewById(R.id.more_detailed_playlist)
        posterForDetailedPlaylistFragment =
            view.findViewById<ImageView>(R.id.poster_for_detailed_playlist_fragment)
        detailedBottomSheetContainer = view.findViewById(R.id.detailed_playlists_bottom_sheet)
        moreDetailedBottomSheetContainer =
            view.findViewById(R.id.more_detailed_playlists_bottom_sheet)
        overlay = view.findViewById(R.id.detailed_overlay)
        tracksDuration = view.findViewById(R.id.detailed_playlist_duration)
        infoBottomImage = view.findViewById(R.id.info_bottom_image)
        infoBottomName = view.findViewById(R.id.info_bottom_name)
        infoBottomAmount = view.findViewById(R.id.info_bottom_amount)
        shareInfoBottomSheet = view.findViewById(R.id.share_info_bottom_sheet)
        editInfoBottomSheet = view.findViewById(R.id.edit_info_bottom_sheet)
        deleteInfoBottomSheet = view.findViewById(R.id.delete_info_bottom_sheet)
        detailedBottomSheetRecycler =
            view.findViewById(R.id.detailed_playlist_bottom_sheet_recyclerview)
        Glide.with(requireActivity())
            .load(playlistData.playlistUri)
            .placeholder(R.drawable.nodata)
            .centerCrop()
            .into(posterForDetailedPlaylistFragment)
        posterName.text = playlistData.playlistName
        if (playlistData.playlistDesc.isNullOrEmpty())
            posterDesc.visibility = View.GONE
        else
            posterDesc.visibility = View.VISIBLE

        posterDesc.text = playlistData.playlistDesc
        trackAmount.text = ConvertUtil.conventAmountToTrackString(playlistData.playlistAmount)
    }

    private fun initBottomBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(detailedBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
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
        moreBottomSheetBehavior = BottomSheetBehavior.from(moreDetailedBottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        moreBottomSheetBehavior.addBottomSheetCallback(object :
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
                overlay.alpha = slideOffset + 1
            }
        })

    }

    companion object {
        fun createArgs(playlistData: PlaylistData): Bundle =
            bundleOf(EDIT_KEY to playlistData)
    }

}