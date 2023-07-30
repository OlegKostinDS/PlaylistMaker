package ru.dsvusial.playlistmaker.music_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.music_library.ui.PlaylistAdapter
import ru.dsvusial.playlistmaker.music_library.ui.model.PlaylistState
import ru.dsvusial.playlistmaker.music_library.ui.view_models.PlaylistViewModel


class PlaylistFragment : Fragment() {
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var addPlaylistButton: Button
    private lateinit var playlistImage: ImageView
    private lateinit var playlistText: TextView
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPlaylistButton = view.findViewById(R.id.new_playlist_btn)
        playlistImage = view.findViewById(R.id.playlist_fragment_image)
        playlistText = view.findViewById(R.id.playlist_nothing_found_text)
        playlistRecyclerView = view.findViewById(R.id.playlist_recyclerview)
        initAdapters()
        addPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_musicLibraryFragment_to_newPlaylistFragment
            )
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistState.Empty -> {
                    playlistImage.visibility = View.VISIBLE
                    playlistText.visibility = View.VISIBLE
                    playlistRecyclerView.visibility = View.GONE
                }

                is PlaylistState.Content -> {
                    playlistImage.visibility = View.GONE
                    playlistText.visibility = View.GONE
                    playlistRecyclerView.visibility = View.VISIBLE
                    showPlaylists(state.playlists)
                }


            }
        }
    }

    private fun showPlaylists(list: List<PlaylistData>) {
        playlistAdapter.recentPlaylists.clear()
        playlistAdapter.recentPlaylists.addAll(list)
        playlistAdapter.notifyDataSetChanged()
    }
    private fun initAdapters() {
        playlistAdapter = PlaylistAdapter {


        }
        playlistRecyclerView.adapter = playlistAdapter
        playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistRecyclerView.adapter?.notifyDataSetChanged()
    }
}