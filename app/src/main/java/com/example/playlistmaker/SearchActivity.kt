package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.api.TrackResponse
import com.example.playlistmaker.api.TracksApi
import com.example.playlistmaker.tracks.Track
import com.example.playlistmaker.tracks.TrackAdapter
import com.example.playlistmaker.utils.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var textValue: String = TEXT_VALUE
    private val apiURL = "https://itunes.apple.com"
    private val tracks: MutableList<Track> = mutableListOf()
    private val adapter = TrackAdapter{
        clickTrack(it)
    }

    // TrackSearch
    private lateinit var sharedSearch: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var adapterSearch: TrackAdapter

    private lateinit var inputEditText: EditText
    private lateinit var trackList: RecyclerView
    private lateinit var trackSeachHistoryList: RecyclerView
    private lateinit var placeholderConnect: LinearLayout
    private lateinit var placeholderFound: TextView
    private val retrofit = Retrofit.Builder()
        .baseUrl(apiURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TracksApi::class.java)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        sharedSearch = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedSearch)
        adapterSearch = TrackAdapter{
            clickHistoryTrack(it)
        }

        inputEditText = findViewById(R.id.input_search_text)
        trackList = findViewById(R.id.searching_list)
        trackSeachHistoryList = findViewById(R.id.searching_history_list)
        placeholderConnect = findViewById(R.id.placeholder_connect)
        placeholderFound = findViewById(R.id.placeholder_found)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val backBtn = findViewById<TextView>(R.id.home)
        val refreshBtn = findViewById<Button>(R.id.btn_refresh)
        val clearHistoryBtn = findViewById<Button>(R.id.btn_clear_history)

        val musicLayout = findViewById<FrameLayout>(R.id.music_layout)
        val searchHistoryLayout = findViewById<LinearLayout>(R.id.search_his_layout)

        adapter.tracks = tracks

        adapterSearch.tracks = searchHistory.tracks

        backBtn.setOnClickListener {
            this.finish()
        }
        refreshBtn.setOnClickListener {
            startSearch()
        }
        clearHistoryBtn.setOnClickListener{
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

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            } else {
                false
            }
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryLayout.visibility = if (
                hasFocus &&
                inputEditText.text.isEmpty() &&
                searchHistory.tracks.size > 0
                ) View.VISIBLE else View.GONE
            musicLayout.visibility = if (hasFocus && inputEditText.text.isEmpty()) View.GONE else View.VISIBLE
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchHistoryLayout.visibility = if (
                    inputEditText.hasFocus() &&
                    s?.isEmpty() == true &&
                    searchHistory.tracks.size > 0
                    ) View.VISIBLE else View.GONE
                musicLayout.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setText(textValue)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter
        trackSeachHistoryList.layoutManager = LinearLayoutManager(this)
        trackSeachHistoryList.adapter = adapterSearch
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textValue = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    private fun startSearch() {
        if (inputEditText.text.isNotEmpty()) {
            trackService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                                showMessage("")
                            } else {
                                showMessage("", notFound = true)
                            }
                        } else {
                            showMessage(response.code().toString(), notConnect = true)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(t.message.toString(), notConnect = true)
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(
        additionalMessage: String,
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
    private fun clickTrack(track: Track){
        searchHistory.addToSavedTrackList(track)
        adapterSearch.notifyDataSetChanged()
    }

    private fun clickHistoryTrack(track: Track){

    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveSearchingList()
    }
}
