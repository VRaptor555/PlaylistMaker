package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.getURLImage500
import com.example.playlistmaker.utils.timeMillisToMin
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {
    private lateinit var playBtn: ImageButton
    private lateinit var timeLeft: TextView
    private lateinit var player: PlayerInteractor

    companion object {
        private const val DALAY_TIMER = 250L

        private const val PLAYER_BUTTON_PLAY = 0
        private const val PLAYER_BUTTON_PAUSE = 1
    }
    var playerButtonState = PLAYER_BUTTON_PLAY

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        val durationInfo = findViewById<TextView>(R.id.duration_info)
        val albumInfo = findViewById<TextView>(R.id.album_info)
        val yearInfo = findViewById<TextView>(R.id.year_info)
        val genreInfo = findViewById<TextView>(R.id.genre_info)
        val countryInfo = findViewById<TextView>(R.id.country_info)
        val backBtn = findViewById<TextView>(R.id.btn_back)

        val imageTrack = findViewById<ImageView>(R.id.image_track)
        val trackTitle = findViewById<TextView>(R.id.track_title)
        val trackArtist = findViewById<TextView>(R.id.track_artist)

        playBtn = findViewById(R.id.play_btn)
        timeLeft = findViewById(R.id.time_left)

        backBtn.setOnClickListener {
            this.finish()
        }

        val track = getSerializable(this, "track", Track::class.java)

        player = Creator.providePlayer(track.previewUrl)

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

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    setCurrentPosition()
                    handler.postDelayed(this, DALAY_TIMER)
                }
            },
            DALAY_TIMER
        )
        playBtn.setOnClickListener {
            player.playback()
        }

    }

    private fun setCurrentPosition() {
        player.preparePlayer(object : PlayerInteractor.PlayerConsumer {
            override fun showTime(currentPosition: Int, currentState: Int) {
                when (currentState) {
                    PlayerRepositoryImpl.STATE_PLAYING -> {
                        if (playerButtonState == PLAYER_BUTTON_PLAY) {
                            playBtn.setImageResource(R.drawable.pause_btn)
                            playerButtonState = PLAYER_BUTTON_PAUSE
                        }
                        timeLeft.text = timeMillisToMin(currentPosition)
                    }
                    PlayerRepositoryImpl.STATE_PREPARED,
                    PlayerRepositoryImpl.STATE_DEFAULT -> {
                        if (playerButtonState == PLAYER_BUTTON_PAUSE) {
                            playBtn.setImageResource(R.drawable.play_btn)
                            playerButtonState = PLAYER_BUTTON_PLAY
                        }
                        timeLeft.text = timeMillisToMin(0)
                    }
                    PlayerRepositoryImpl.STATE_PAUSED -> {
                        if (playerButtonState == PLAYER_BUTTON_PAUSE) {
                            playBtn.setImageResource(R.drawable.play_btn)
                            playerButtonState = PLAYER_BUTTON_PLAY
                        }
                        timeLeft.text = timeMillisToMin(currentPosition)
                    }
                }
            }
        })
    }


    override fun onPause() {
        super.onPause()
        if (player.state() == PlayerRepositoryImpl.STATE_PLAYING) {
            player.playback()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}