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

    private var viewModel: TrackSearchViewModel? = null
    private var binding: ActivitySearchBinding? = null

    private var isClickAllowed = true
    private val handlerDebounce = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

    private var textWatcher: TextWatcher? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = ViewModelProvider(this, TrackSearchViewModel.getViewModelFactory()) [TrackSearchViewModel::class.java]
        viewModel?.let { model ->
            model.observeState().observe(this) {
                render(it)
            }
        }

        binding?.let { bind ->
            bind.searchingList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            bind.searchingList.adapter = searchingAdapter
            bind.searchingHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            bind.searchingHistoryList.adapter = historyAdapter

            bind.home.setOnClickListener { this.finish() }
            bind.btnClearHistory.setOnClickListener { viewModel?.clearHistoryList() }
            bind.btnRefresh.setOnClickListener {
                if (bind.inputSearchText.text.isNotEmpty()) {
                    viewModel?.restartSearch()
                }
            }
            bind.clearIcon.setOnClickListener {
                bind.inputSearchText.setText("")
                searchingAdapter.tracks.clear()
                searchingAdapter.notifyDataSetChanged()
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(bind.inputSearchText.windowToken, 0)
                viewModel?.getHistoryTrackList()

            }
            bind.inputSearchText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus && bind.inputSearchText.text.isEmpty()) {
                    viewModel?.getHistoryTrackList()
                }
            }
        }



        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchDebounce(
                    searchText = s?.toString() ?: ""
                )
                if ((s?.toString() ?: "") == "") {
                    viewModel?.getHistoryTrackList()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        textWatcher?.let { binding?.inputSearchText?.addTextChangedListener(it)}
        binding?.inputSearchText?.setText(textValue)
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
        textWatcher.let { binding?.inputSearchText?.removeTextChangedListener(it) }
    }

    private fun clickTrack(track: Track) {
        viewModel?.addTrackToHistory(track)
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
        binding?.let {
            it.tracksLayout.isVisible = true
            it.searchHisLayout.isVisible = false
            it.progressBar.isVisible = true
            it.placeholderFound.isVisible = false
            it.placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showError(errorMessage: String) {
        binding?.let {
            it.tracksLayout.isVisible = true
            it.searchHisLayout.isVisible = false
            it.progressBar.isVisible = false
            it.placeholderFound.isVisible = false
            it.placeholderConnect.isVisible = true
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showEmpty(message: String) {
        binding?.let {
            it.tracksLayout.isVisible = true
            it.searchHisLayout.isVisible = false
            it.progressBar.isVisible = false
            it.placeholderFound.isVisible = true
            it.placeholderConnect.isVisible = false
        }
        refreshSearchAdapter(emptyList())
    }

    private fun showContent(tracks: List<Track>) {
        binding?.let {
            it.tracksLayout.isVisible = true
            it.searchHisLayout.isVisible = false
            it.progressBar.isVisible = false
            it.placeholderFound.isVisible = false
            it.placeholderConnect.isVisible = false
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
        binding?.let {
            it.searchHisLayout.isVisible = false
            it.tracksLayout.isVisible = false
        }
        refreshHistoryAdapter(emptyList())
    }

    private fun showHistoryContent(tracks: List<Track>) {
        binding?.let {
            it.searchHisLayout.isVisible = true
            it.tracksLayout.isVisible = false
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

}
