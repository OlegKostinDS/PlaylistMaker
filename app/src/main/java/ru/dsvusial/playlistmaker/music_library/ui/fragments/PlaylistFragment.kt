package ru.dsvusial.playlistmaker.music_library.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.music_library.ui.view_models.PlaylistViewModel


class PlaylistFragment : Fragment() {
    private val viewModel by viewModels<PlaylistViewModel>()

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }


}