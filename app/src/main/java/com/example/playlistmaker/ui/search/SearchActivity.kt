package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TracksHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.tracks.PLAYLIST_MAKER_PREFERENCES

class SearchActivity : AppCompatActivity() {
    private var textValue: String = TEXT_VALUE
    private val tracks: MutableList<Track> = mutableListOf()
    private val adapter = TrackAdapter {
        if (clickDebounce())
            clickTrack(it)
    }
    private val creator = Creator.provideTracksInteractor()
    private var priorSearch: String = ""

    // TrackSearch
    private lateinit var searchHistory: TracksHistoryInteractor
    private lateinit var adapterSearch: TrackAdapter

    private lateinit var inputEditText: EditText
    private lateinit var trackList: RecyclerView
    private lateinit var trackSearchHistoryList: RecyclerView
    private lateinit var placeholderConnect: LinearLayout
    private lateinit var placeholderFound: TextView
    private lateinit var progressBar: ProgressBar

    private var bStopHandler = true
    private var isClickAllowed = true
    private val handlerDebounce = Handler(Looper.getMainLooper())
    private val handlerFindDebounce = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val FIND_DEBOUNCE_DELAY = 3
        private const val REFRESH_DELAY = 1_000L
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        adapterSearch = TrackAdapter {
            clickHistoryTrack(it)
        }

        inputEditText = findViewById(R.id.input_search_text)
        trackList = findViewById(R.id.searching_list)
        trackSearchHistoryList = findViewById(R.id.searching_history_list)
        placeholderConnect = findViewById(R.id.placeholder_connect)
        placeholderFound = findViewById(R.id.placeholder_found)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val backBtn = findViewById<TextView>(R.id.home)
        val refreshBtn = findViewById<Button>(R.id.btn_refresh)
        val clearHistoryBtn = findViewById<Button>(R.id.btn_clear_history)
        progressBar = findViewById(R.id.progress_bar)

        val musicLayout = findViewById<FrameLayout>(R.id.music_layout)
        val searchHistoryLayout = findViewById<LinearLayout>(R.id.search_his_layout)

        adapter.tracks = tracks

        searchHistory = Creator.provideHistory(getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE), SEARCH_TEXT)
        adapterSearch.tracks = searchHistory.getSavedTracksList()

        backBtn.setOnClickListener {
            this.finish()
        }
        refreshBtn.setOnClickListener {
            startSearch()
        }
        clearHistoryBtn.setOnClickListener {
            searchHistory.clearTracks()
            adapterSearch.notifyDataSetChanged()
            searchHistoryLayout.visibility = View.GONE
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            showMessage("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if (
                hasFocus &&
                inputEditText.text.isEmpty() &&
                searchHistory.size() > 0
            ) View.VISIBLE else View.GONE
            musicLayout.visibility =
                if (hasFocus && inputEditText.text.isEmpty()) View.GONE else View.VISIBLE
        }

        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter
        trackSearchHistoryList.layoutManager = LinearLayoutManager(this)
        trackSearchHistoryList.adapter = adapterSearch

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchHistoryLayout.visibility = if (
                    inputEditText.hasFocus() &&
                    s?.isEmpty() == true &&
                    searchHistory.size() > 0
                ) View.VISIBLE else View.GONE
                musicLayout.visibility =
                    if (inputEditText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
                if (
                    !bStopHandler ||
                    inputEditText.text.isEmpty() ||
                    inputEditText.text.equals(priorSearch)
                ) return@onTextChanged
                var delay = FIND_DEBOUNCE_DELAY
                bStopHandler = false
                priorSearch = inputEditText.text.toString()
                tracks.clear()
                adapter.notifyDataSetChanged()
                progressBar.visibility = ProgressBar.INVISIBLE
                handlerFindDebounce.postDelayed(
                    object : Runnable {
                        override fun run() {
                            if (delay <= 0) {
                                bStopHandler = true
                                progressBar.visibility = ProgressBar.VISIBLE
                                startSearch()
                            } else {
                                delay--
                            }
                            if (!bStopHandler) {
                                handlerFindDebounce.postDelayed(
                                    this,
                                    REFRESH_DELAY
                                )
                            }
                        }
                    },
                    REFRESH_DELAY
                )
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setText(textValue)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textValue = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun startSearch() {
        val searchHandler = Handler(Looper.getMainLooper())

        if (inputEditText.text.isNotEmpty()) {
            creator.searchTracks(
                inputEditText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        tracks.clear()
                        if (foundTracks.isNotEmpty()) {
                            tracks.addAll(foundTracks)
                            searchHandler.post {
                                adapter.notifyDataSetChanged()
                                showMessage()
                            }
                        } else {
                            searchHandler.post {
                                showMessage("", notFound = true)
                            }
                        }
                    }

                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(
        additionalMessage: String = "",
        notConnect: Boolean = false,
        notFound: Boolean = false
    ) {
        if (notConnect || notFound) {
            tracks.clear()
            adapter.notifyDataSetChanged()
            if (notConnect) {
                placeholderFound.visibility = View.GONE
                placeholderConnect.visibility = View.VISIBLE
            } else {
                placeholderConnect.visibility = View.GONE
                placeholderFound.visibility = View.VISIBLE
            }
            progressBar.visibility = ProgressBar.INVISIBLE
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderFound.visibility = View.GONE
            placeholderConnect.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickTrack(track: Track) {
        searchHistory.addToSavedTracksList(track)
        adapterSearch.notifyDataSetChanged()
        clickHistoryTrack(track)
    }

    private fun clickHistoryTrack(track: Track) {
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra("track", track)
        startActivity(playerIntent)
    }


    override fun onStop() {
        super.onStop()
        searchHistory.saveTracksList()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerDebounce.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}
