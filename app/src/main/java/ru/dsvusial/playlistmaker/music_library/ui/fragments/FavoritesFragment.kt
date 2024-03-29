package ru.dsvusial.playlistmaker.music_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.music_library.ui.model.FavoritesTrackState
import ru.dsvusial.playlistmaker.music_library.ui.view_models.FavoritesViewModel
import ru.dsvusial.playlistmaker.search.ui.SEARCH_KEY
import ru.dsvusial.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import ru.dsvusial.playlistmaker.search.ui.TrackAdapter
import ru.dsvusial.playlistmaker.utils.debounce


class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var favotiesNothingFoundImage: ImageView
    private lateinit var favoritesNothingFoundText: TextView
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var onClickDebounce: (TrackData) -> Unit
    companion object {
        fun newInstance() = FavoritesFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favotiesNothingFoundImage = view.findViewById(R.id.favorites_nothing_found_image)
        favoritesNothingFoundText = view.findViewById(R.id.favorites_nothing_found_text)
        favoritesRecyclerView = view.findViewById(R.id.favorites_tracks_recyclerview)
        initAdapters()
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            onClickDebounce =     debounce(delayMillis = CLICK_DEBOUNCE_DELAY,
                coroutineScope = viewLifecycleOwner.lifecycleScope,
                useLastParam = false,
                action = {
                    findNavController().navigate(
                        R.id.action_musicLibraryFragment_to_mediaPlayerFragment,
                        bundleOf(SEARCH_KEY to it)
                    )
                })
            when (state) {
                FavoritesTrackState.Empty -> {
                    favotiesNothingFoundImage.visibility = View.VISIBLE
                    favoritesNothingFoundText.visibility = View.VISIBLE
                    favoritesRecyclerView.visibility = View.GONE
                }

                is FavoritesTrackState.Content -> {
                    favotiesNothingFoundImage.visibility = View.GONE
                    favoritesNothingFoundText.visibility = View.GONE
                    favoritesRecyclerView.visibility = View.VISIBLE
                    showFavorites(state.tracks)
                }
            }
        }

    }

    private fun showFavorites(list: List<TrackData>) {
        trackAdapter.recentTracks.clear()
        trackAdapter.recentTracks.addAll(list)
        trackAdapter.notifyDataSetChanged()
    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter ()
trackAdapter.onItemClick = {
        track->
    onClickDebounce(track)
}
        favoritesRecyclerView.adapter = trackAdapter
        favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoritesRecyclerView.adapter?.notifyDataSetChanged()
    }


}