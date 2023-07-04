package ru.dsvusial.playlistmaker.music_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.dsvusial.playlistmaker.R

class MusicLibraryFragment : Fragment() {
    companion object {
        fun newInstance() = MusicLibraryFragment()
    }
    private lateinit var libraryViewpager: ViewPager2
    private lateinit var libraryTabLayout: TabLayout
    private lateinit var tabMediator: TabLayoutMediator


    private lateinit var mLibraryToolbar: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_music_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        libraryViewpager.adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(libraryTabLayout, libraryViewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }

        }
        tabMediator.attach()

    }

    private fun initViews(view: View) {
        mLibraryToolbar = view.findViewById(R.id.m_library_toolbar)
        libraryViewpager = view.findViewById(R.id.library_viewpager)
        libraryTabLayout = view.findViewById(R.id.library_tab_layout)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}