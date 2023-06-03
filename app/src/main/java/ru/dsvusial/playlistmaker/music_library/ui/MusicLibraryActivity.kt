package ru.dsvusial.playlistmaker.music_library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.utils.router.MusicLibraryRouter


class MusicLibraryActivity : AppCompatActivity() {

    private lateinit var libraryViewpager: ViewPager2
    private lateinit var libraryTabLayout: TabLayout
    private lateinit var tabMediator: TabLayoutMediator

    private val mLibraryRouter by lazy { MusicLibraryRouter(this) }

    private lateinit var mLibraryToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_library)
        initViews()
        initListeners()
        libraryViewpager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(libraryTabLayout, libraryViewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }

        }
        tabMediator.attach()
    }

    private fun initViews() {
        mLibraryToolbar = findViewById(R.id.m_library_toolbar)
        libraryViewpager = findViewById(R.id.library_viewpager)
        libraryTabLayout = findViewById(R.id.library_tab_layout)
    }

    private fun initListeners() {
        mLibraryToolbar.setOnClickListener { mLibraryRouter.goBack() }
    }


}