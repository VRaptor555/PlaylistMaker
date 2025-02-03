package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.models.TracksState
import com.example.playlistmaker.search.ui.view_model.TrackSearchViewModel


class SearchActivity : AppCompatActivity() {
    private var textValue: String = TEXT_VALUE
    private val historyAdapter = TrackAdapter(
        object : TrackAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce())
                    clickHistoryTrack(track)
            }
        }
    )

    private val searchingAdapter = TrackAdapter(
        object : TrackAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebounce())
                    clickTrack(track)
            }
        }
    )

    private lateinit var viewModel: TrackSearchViewModel
    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true
    private val handlerDebounce = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

    private lateinit var textWatcher: TextWatcher

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, TrackSearchViewModel.getViewModelFactory()) [TrackSearchViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.searchingList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchingList.adapter = searchingAdapter
        binding.searchingHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchingHistoryList.adapter = historyAdapter


        binding.home.setOnClickListener {
            this.finish()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistoryList()
        }

        binding.btnRefresh.setOnClickListener {
            if (binding.inputSearchText.text.isNotEmpty()) {
                viewModel.restartSearch()
            }
        }

        binding.clearIcon.setOnClickListener {
            binding.inputSearchText.setText("")
            searchingAdapter.tracks.clear()
            searchingAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputSearchText.windowToken, 0)
            viewModel.getHistoryTrackList()
        }

        binding.inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearchText.text.isEmpty()) {
                viewModel.getHistoryTrackList()
            }
        }


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    searchText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        textWatcher.let { binding.inputSearchText.addTextChangedListener(it)}
        binding.inputSearchText.setText(textValue)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textValue = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher.let { binding.inputSearchText.removeTextChangedListener(it) }
    }

    private fun clickTrack(track: Track) {
        viewModel.addTrackToHistory(track)
        clickHistoryTrack(track)
    }

    private fun clickHistoryTrack(track: Track) {
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra("track", track)
        startActivity(playerIntent)
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerDebounce.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshSearchAdapter(tracks: List<Track>) {
        searchingAdapter.tracks.clear()
        searchingAdapter.tracks.addAll(tracks)
        searchingAdapter.notifyDataSetChanged()
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
        with(binding) {
            tracksLayout.isVisible = true
            searchHisLayout.isVisible = false
            progressBar.isVisible = false
            placeholderFound.isVisible = false
            placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(tracks)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshHistoryAdapter(tracks: List<Track>) {
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(tracks)
        historyAdapter.notifyDataSetChanged()
    }

    private fun showHistoryEmpty() {
        binding.searchHisLayout.isVisible = false
        binding.tracksLayout.isVisible = false
        refreshHistoryAdapter(emptyList())
    }

    private fun showHistoryContent(tracks: List<Track>) {
        binding.searchHisLayout.isVisible = true
        binding.tracksLayout.isVisible = false
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

}
