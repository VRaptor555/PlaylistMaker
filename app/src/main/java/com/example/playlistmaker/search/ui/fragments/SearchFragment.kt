package com.example.playlistmaker.search.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.example.playlistmaker.main.ui.utils.PlaylistTracksCallback
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.models.TracksState
import com.example.playlistmaker.search.ui.view_model.TrackSearchViewModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: BindingFragments<FragmentSearchBinding>() {
    private val viewModel: TrackSearchViewModel by viewModel()
    private var textValue: String = TEXT_VALUE
    private var isHistory: Boolean = false

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onHistoryTrackClickDebounce: (Track) -> Unit
    private lateinit var onHistoryGet: (Int) -> Unit

    private var searchingAdapter: TrackAdapter? = null
    private var historyAdapter: TrackAdapter? = null

    private var textWatcher: TextWatcher? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> clickTrack(track) }

        onHistoryTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> clickHistoryTrack(track) }

        onHistoryGet = debounce (
            HIST_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { viewModel.getHistoryTrackList() }

        searchingAdapter = TrackAdapter(
            object : TrackAdapter.TrackClickListener {
                override fun onTrackClick(track: Track) {
                    onTrackClickDebounce(track)
                }

                override fun onLongTrackClick(track: Track): Boolean {
                    return false
                }
            }
        )
        historyAdapter = TrackAdapter(
            object : TrackAdapter.TrackClickListener {
                override fun onTrackClick(track: Track) {
                    onHistoryTrackClickDebounce(track)
                }

                override fun onLongTrackClick(track: Track): Boolean {
                    return true
                }
            }
        )

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchingList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchingList.adapter = searchingAdapter
        binding.searchingHistoryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchingHistoryList.adapter = historyAdapter

        binding.btnClearHistory.setOnClickListener { viewModel.clearHistoryList() }
        binding.btnRefresh.setOnClickListener {
            if (binding.inputSearchText.text.isNotEmpty()) {
                viewModel.restartSearch()
            }
        }
        binding.clearIcon.setOnClickListener {
            binding.inputSearchText.setText("")
        }

        binding.inputSearchText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputSearchText.text.isEmpty()) {
                onHistoryGet(1)
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchTracks(s?.toString() ?: "")
                if ((s?.toString() ?: "") == "") {
                    onHistoryGet(1)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        textWatcher?.let { binding.inputSearchText.addTextChangedListener(it)}
        binding.inputSearchText.setText(textValue)
    }

    private fun clickTrack(track: Track) {
        viewModel.addTrackToHistory(track)
        clickHistoryTrack(track)
    }

    private fun clickHistoryTrack(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerActivity,
            PlayerActivity.createArgs(track)
        )
    }

    private fun refreshSearchAdapter(tracks: List<Track>) {
        searchingAdapter?.let {
            val diffTrackCalback = PlaylistTracksCallback(it.tracks.toList(), tracks)
            val diffTracks = DiffUtil.calculateDiff(diffTrackCalback)
            it.tracks.clear()
            it.tracks.addAll(tracks)
            diffTracks.dispatchUpdatesTo(it)
        }
    }

    private fun showLoading() {
        with(binding) {
            tracksLayout.isVisible = true
            searchHisLayout.isVisible = false
            progressBar.isVisible = true
            placeholderFound.isVisible = false
            placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            tracksLayout.isVisible = true
            searchHisLayout.isVisible = false
            progressBar.isVisible = false
            placeholderFound.isVisible = false
            placeholderConnect.isVisible = true
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showEmpty(message: String) {
        isHistory = false
        with(binding) {
            tracksLayout.isVisible = true
            searchHisLayout.isVisible = false
            progressBar.isVisible = false
            placeholderFound.isVisible = true
            placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showContent(tracks: List<Track>) {
        isHistory = false
        with(binding) {
            tracksLayout.isVisible = true
            searchHisLayout.isVisible = false
            progressBar.isVisible = false
            placeholderFound.isVisible = false
            placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(tracks)
    }

    private fun refreshHistoryAdapter(tracks: List<Track>) {
        historyAdapter?.let {
            val diffTrackCalback = PlaylistTracksCallback(it.tracks.toList(), tracks)
            val diffTracks = DiffUtil.calculateDiff(diffTrackCalback)
            it.tracks.clear()
            it.tracks.addAll(tracks)
            diffTracks.dispatchUpdatesTo(it)
        }
    }

    private fun showHistoryEmpty() {
        isHistory = true
        with(binding) {
            searchHisLayout.isVisible = false
            tracksLayout.isVisible = false
        }
        refreshHistoryAdapter(emptyList())
    }

    private fun showHistoryContent(tracks: List<Track>) {
        isHistory = true
        with(binding) {
            searchHisLayout.isVisible = true
            tracksLayout.isVisible = false
        }
        refreshHistoryAdapter(tracks)
    }

    private fun render(state: TracksState) {
        when(state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.ContentHistory -> showHistoryContent(state.tracks)
            is TracksState.EmptyHistory -> showHistoryEmpty()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textValue)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textValue = savedInstanceState?.getString(SEARCH_TEXT, "") ?: ""
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavorite(isHistory)
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val HIST_DEBOUNCE_DELAY = 100L
    }
}