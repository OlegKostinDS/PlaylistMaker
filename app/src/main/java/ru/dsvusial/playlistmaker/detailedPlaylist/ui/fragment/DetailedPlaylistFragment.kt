package ru.dsvusial.playlistmaker.detailedPlaylist.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.databinding.FragmentDetailedPlaylistBinding
import ru.dsvusial.playlistmaker.detailedPlaylist.ui.model.BottomSheetState
import ru.dsvusial.playlistmaker.detailedPlaylist.ui.viewmodel.DetailedPlaylistViewModel
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.ui.PLAYLIST_KEY
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY
import ru.dsvusial.playlistmaker.search.ui.TrackAdapter
import ru.dsvusial.playlistmaker.utils.ConvertUtil
import ru.dsvusial.playlistmaker.utils.DateTimeUtil


const val EDIT_KEY = "edit_key"

class DetailedPlaylistFragment : Fragment() {
    private var _binding: FragmentDetailedPlaylistBinding? = null
    private val binding get() = _binding!!
    val viewModel by viewModel<DetailedPlaylistViewModel>()
    private lateinit var playlistData: PlaylistData
    private lateinit var currentTracks: List<TrackData>
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var moreBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var deleteDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailedPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistData = requireArguments().getSerializable(PLAYLIST_KEY)!! as PlaylistData
        viewModel.fillData(playlistData)
        initUI()
        initBottomBehavior()
        initListeners(playlistData)

        viewModel.getTracks().observe(viewLifecycleOwner) { tracks ->
            currentTracks = tracks

            binding.detailedPlaylistDuration.text =
                ConvertUtil.conventAmountToMinutesString(
                    DateTimeUtil.formatTimeMillisToMinutesString(
                        tracks.sumOf { it.trackTimeMillis })
                )

            trackAdapter = TrackAdapter(imageQuality = "60x60bb.jpg")
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

            binding.detailedPlaylistBottomSheetRecyclerview.adapter = trackAdapter
            binding.detailedPlaylistBottomSheetRecyclerview.layoutManager =
                LinearLayoutManager(requireContext())
            trackAdapter.notifyDataSetChanged()
        }

        viewModel.getPlaylist().observe(viewLifecycleOwner) { playlist ->
            binding.tracksAmountDetailed.text =
                ConvertUtil.conventAmountToTrackString(playlist.playlistAmount)
            trackAdapter = TrackAdapter()
            trackAdapter.notifyDataSetChanged()
        }

        viewModel.isBottomSheetClosed().observe(viewLifecycleOwner) { state ->
            if (state) {
                moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            } else {
                moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BottomSheetState.Content -> {
                    binding.detailedPlaylistBottomSheetNoData.visibility = View.GONE
                    binding.detailedPlaylistBottomSheetRecyclerview.visibility = View.VISIBLE

                }

                BottomSheetState.Empty -> {
                    binding.detailedPlaylistBottomSheetNoData.visibility = View.VISIBLE
                    binding.detailedPlaylistBottomSheetRecyclerview.visibility = View.GONE
                }
            }
        }
    }

    private fun initListeners(playlistData: PlaylistData) {
        binding.detailedBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.moreDetaliedPlaylistContainer.setOnClickListener {

        }
        binding.shareDetailedPlaylist.setOnClickListener {

            sharePlaylist()
        }
        binding.shareInfoBottomSheet.setOnClickListener {
            viewModel.closeBottomSheet(true)
            sharePlaylist()
        }
        binding.moreDetailedPlaylist.setOnClickListener {
            val cornerRadius =
                requireActivity().resources.getDimensionPixelSize(R.dimen.lesser_btn_radius)
            Glide.with(requireActivity())
                .load(playlistData.playlistUri)
                .placeholder(R.drawable.nodata)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .into(binding.infoBottomImage)
            binding.infoBottomName.text = playlistData.playlistName
            binding.infoBottomAmount.text =
                ConvertUtil.conventAmountToTrackString(playlistData.playlistAmount)
            viewModel.closeBottomSheet(false)
        }

        binding.editInfoBottomSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailedPlaylistFragment_to_editPlaylistFragment,
                createArgs(playlistData)
            )
        }
        binding.deleteInfoBottomSheet.setOnClickListener {
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
        if (currentTracks.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val intent = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getSharingTextFromPlaylist(playlistData, currentTracks))
                type = "text/plain"
            }, getString(R.string.share))
            requireActivity().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
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
                "${index + 1}. ${track.artistName} - ${track.trackName} (${
                    DateTimeUtil.formatTimeMillisToString(track.trackTimeMillis)
                })"
            )
        }
        return tempString.joinToString("\n")

    }

    private fun initUI() {
        Glide.with(requireActivity())
            .load(playlistData.playlistUri)
            .placeholder(R.drawable.nodata)
            .centerCrop()
            .into(binding.posterForDetailedPlaylistFragment)
        binding.detailedPlaylistName.text = playlistData.playlistName
        if (playlistData.playlistDesc.isNullOrEmpty())
            binding.detailedPlaylistDescription.visibility = View.GONE
        else
            binding.detailedPlaylistDescription.visibility = View.VISIBLE

        binding.detailedPlaylistDescription.text = playlistData.playlistDesc
        binding.tracksAmountDetailed.text =
            ConvertUtil.conventAmountToTrackString(playlistData.playlistAmount)
        viewModel.closeBottomSheet(true)
    }

    private fun initBottomBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.detailedPlaylistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.detailedOverlay.visibility = View.GONE
                    }

                    else -> {
                        binding.detailedOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.detailedOverlay.alpha = slideOffset
            }
        })
        moreBottomSheetBehavior =
            BottomSheetBehavior.from(binding.moreDetailedPlaylistsBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        moreBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.detailedOverlay.visibility = View.GONE
                    }

                    else -> {
                        binding.detailedOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.detailedOverlay.alpha = slideOffset + 1
            }
        })

    }

    companion object {
        fun createArgs(playlistData: PlaylistData): Bundle =
            bundleOf(EDIT_KEY to playlistData)
    }

}