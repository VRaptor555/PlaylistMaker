package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.tracks.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.getURLImage500
import com.example.playlistmaker.utils.timeMillisToMin
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        val timeLeft = findViewById<TextView>(R.id.time_left)
        val durationInfo = findViewById<TextView>(R.id.duration_info)
        val albumInfo = findViewById<TextView>(R.id.album_info)
        val yearInfo = findViewById<TextView>(R.id.year_info)
        val genreInfo = findViewById<TextView>(R.id.genre_info)
        val countryInfo = findViewById<TextView>(R.id.country_info)
        val backBtn = findViewById<TextView>(R.id.btn_back)

        val imageTrack = findViewById<ImageView>(R.id.image_track)
        val trackTitle = findViewById<TextView>(R.id.track_title)
        val trackArtist = findViewById<TextView>(R.id.track_artist)

        backBtn.setOnClickListener {
            this.finish()
        }

        val track: Track = getSerializable(this, "track", Track::class.java)

        timeLeft.text = timeMillisToMin(track.trackTimeMillis)
        durationInfo.text = timeMillisToMin(track.trackTimeMillis)
        albumInfo.text = track.collectionName
        yearInfo.text =
            LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
        genreInfo.text = track.primaryGenreName
        countryInfo.text = track.country
        trackTitle.text = track.trackName
        trackArtist.text = track.artistName

        Glide.with(baseContext)
            .load(getURLImage500(track.artworkUrl100))
            .placeholder(R.drawable.player_placeholder)
            .transform(RoundedCorners(dpToPx(8f, baseContext)))
            .into(imageTrack)

    }

}