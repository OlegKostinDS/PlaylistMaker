package ru.dsvusial.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.mediaPlayer.domain.model.TrackData
import ru.dsvusial.playlistmaker.search.domain.model.SearchUIType
import ru.dsvusial.playlistmaker.search.ui.model.UiState
import ru.dsvusial.playlistmaker.search.ui.view_model.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var searchEditText: EditText
    private lateinit var searchTracksRecyclerView: RecyclerView
    private lateinit var searchHistoryTracksRecyclerView: RecyclerView
    private lateinit var searchNothingToFindLayout: ConstraintLayout
    private lateinit var searchNothingFoundImage: ImageView
    private lateinit var searchNothingFoundText: TextView
    private lateinit var searchNothingFoundBtn: Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var searchClearBtn: ImageView
    private lateinit var clearHistoryBtn: Button
    private lateinit var recentHistoryLayout: ConstraintLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var trackAdapter: TrackAdapter
    private var isClickAllowed = true
    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        fun newInstance() = SearchFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI(view)

        viewModel.observeUiStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    showLoading()
                }

                is UiState.HistoryContent -> {
                    showHistory(it.list)
                }

                is UiState.SearchContent -> {
                    showContent(it.list)
                }

                is UiState.Error -> {
                    selectSearchUI(uiType = it.error)
                }
            }
        }

        viewModel.observeTextWatcherStateLiveData().observe(viewLifecycleOwner) {
            searchClearBtn.visibility = clearButtonVisibility(it)
        }


        val textWatcherSearchBtn = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchTextHasChanged(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        searchEditText.addTextChangedListener(textWatcherSearchBtn)
        searchClearBtn.setOnClickListener {
            trackAdapter.recentTracks.clear()
            trackAdapter.notifyDataSetChanged()
            searchEditText.setText("")

            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }


        initAdapters()
        clearHistoryBtn.setOnClickListener {
            viewModel.onClickClearHistoryBtn()
        }
    }

    private fun initializeUI(view: View) {
        toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        searchEditText = view.findViewById(R.id.search_search)
        searchNothingToFindLayout = view.findViewById(R.id.search_nothing_found_layout)
        searchNothingFoundImage = view.findViewById(R.id.search_nothing_found_image)
        searchNothingFoundText = view.findViewById(R.id.search_nothing_found_text)
        searchNothingFoundBtn = view.findViewById(R.id.search_nothing_found_btn)
        searchClearBtn = view.findViewById(R.id.search_cancel_btn)
        searchTracksRecyclerView = view.findViewById(R.id.search_tracks_recyclerview)
        searchHistoryTracksRecyclerView = view.findViewById(R.id.search_history_tracks_recyclerview)
        recentHistoryLayout = view.findViewById(R.id.recent_history_layout)
        clearHistoryBtn = view.findViewById(R.id.clear_history)
        progressbar = view.findViewById(R.id.progressBar)
        searchClearBtn.visibility = View.GONE

    }

    private fun showContent(list: List<TrackData>) {
        progressbar.visibility = View.GONE
        recentHistoryLayout.visibility = View.GONE
        searchTracksRecyclerView.visibility = View.VISIBLE
        searchNothingToFindLayout.visibility = View.GONE
        searchNothingFoundBtn.visibility = View.GONE
        trackAdapter.recentTracks.clear()
        trackAdapter.recentTracks.addAll(list)
        trackAdapter.notifyDataSetChanged()

    }

    private fun showHistory(list: List<TrackData>) {
        if (list.isNotEmpty())
            recentHistoryLayout.visibility = View.VISIBLE
        else
            recentHistoryLayout.visibility = View.GONE
        progressbar.visibility = View.GONE
        searchTracksRecyclerView.visibility = View.GONE
        searchNothingToFindLayout.visibility = View.GONE
        searchNothingFoundBtn.visibility = View.GONE
        historyTrackAdapter.recentTracks.clear()
        historyTrackAdapter.recentTracks.addAll(list)
        historyTrackAdapter.notifyDataSetChanged()
    }

    private fun selectSearchUI(uiType: SearchUIType) {
        when (uiType) {
            SearchUIType.NO_INTERNET -> {
                recentHistoryLayout.visibility = View.GONE
                searchTracksRecyclerView.visibility = View.GONE
                searchNothingToFindLayout.visibility = View.VISIBLE
                searchNothingFoundBtn.visibility = View.VISIBLE
                searchNothingFoundImage.setImageResource(R.drawable.search_no_internet)
                searchNothingFoundText.text = getText(R.string.search_no_internet_text)
                searchNothingFoundBtn.visibility = View.VISIBLE
                progressbar.visibility = View.GONE
                searchNothingFoundBtn.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.search(searchEditText.text.toString())
                    }
                }
            }

            SearchUIType.NO_DATA -> {
                progressbar.visibility = View.GONE
                recentHistoryLayout.visibility = View.GONE
                searchNothingFoundBtn.visibility = View.GONE
                searchTracksRecyclerView.visibility = View.GONE
                searchNothingToFindLayout.visibility = View.VISIBLE
                searchNothingFoundImage.setImageResource(R.drawable.search_nothing_found)
                searchNothingFoundText.text =
                    getText(R.string.search_nothing_find_text)
            }
        }
    }


    private fun showLoading() {
        progressbar.visibility = View.VISIBLE
        recentHistoryLayout.visibility = View.GONE
        searchTracksRecyclerView.visibility = View.GONE
        searchNothingToFindLayout.visibility = View.GONE
        searchNothingFoundBtn.visibility = View.GONE

    }

    private fun initAdapters() {
        trackAdapter = TrackAdapter {
            if (clickDebounce()) {
                viewModel.addToRecentHistoryList(it)
                findNavController().navigate(
                    R.id.action_searchFragment_to_mediaPlayerActivity,
                    bundleOf(SEARCH_KEY to it)
                )
            }
        }

        searchTracksRecyclerView.adapter = trackAdapter
        searchTracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyTrackAdapter = TrackAdapter {
            if (clickDebounce()) {
                viewModel.addToRecentHistoryList(it)
                findNavController().navigate(
                    R.id.action_searchFragment_to_mediaPlayerActivity,
                    bundleOf(SEARCH_KEY to it)
                )
            }
        }
        searchHistoryTracksRecyclerView.adapter = historyTrackAdapter
        searchHistoryTracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryTracksRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun clearButtonVisibility(s: Boolean): Int {
        return if (!s) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}