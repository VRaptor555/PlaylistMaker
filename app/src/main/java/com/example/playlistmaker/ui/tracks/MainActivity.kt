package com.example.playlistmaker.ui.tracks

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val DARK_THEME_KEY = "key_dark_theme"
const val SEARCH_HISTORY_KEY = "key_search_history"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn)
        val libraryBtn = findViewById<Button>(R.id.library_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        searchBtn.setOnClickListener(searchClickListener)

        libraryBtn.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }
        settingsBtn.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        (applicationContext as App).saveConfiguration()
    }
}