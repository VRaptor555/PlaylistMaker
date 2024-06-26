package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var textValue: String = TEXT_VALUE
    private val apiURL = "https://itunes.apple.com"
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private lateinit var inputEditText: EditText
    private lateinit var trackList: RecyclerView
    private lateinit var placeholderConnect: LinearLayout
    private lateinit var placeholderFound: TextView
    private val retrofit = Retrofit.Builder()
        .baseUrl(apiURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val trackService = retrofit.create(TracksApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.inputSearchText)
        trackList = findViewById(R.id.searchingList)
        placeholderConnect = findViewById(R.id.placeholder_connect)
        placeholderFound = findViewById(R.id.placeholder_found)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backBtn = findViewById<TextView>(R.id.home)
        val refreshBtn = findViewById<Button>(R.id.btn_refresh)

        adapter.tracks = tracks

        backBtn.setOnClickListener {
            this.finish()
        }
        refreshBtn.setOnClickListener{
            startSearch()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startSearch()
                true
            } else
            {
                false
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setText(textValue)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textValue = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    private fun startSearch(){
        if (inputEditText.text.isNotEmpty()) {
            trackService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code()==200){
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true){
                            tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                            showMessage("")
                        } else {
                            showMessage( "", notFound = true)
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
    private fun showMessage(additionalMessage: String, notConnect: Boolean = false, notFound: Boolean = false) {
        if (notConnect || notFound) {
            tracks.clear()
            adapter.notifyDataSetChanged()
            if (notConnect) {
                placeholderFound.visibility = View.GONE
                placeholderConnect.visibility = View.VISIBLE
            }
            else {
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

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val TEXT_VALUE = ""
        private val TRACK_LIST = listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),

            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),

            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),

            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),

            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
    }

}
