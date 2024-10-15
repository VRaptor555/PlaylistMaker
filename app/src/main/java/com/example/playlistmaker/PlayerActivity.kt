package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import com.example.playlistmaker.tracks.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.getURLImage500
import com.example.playlistmaker.utils.timeMillisToMin
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var playBtn: ImageButton
    private lateinit var timeLeft: TextView
    private var mediaPlayer = MediaPlayer()
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DALAY_TIMER = 250L
    }
    private lateinit var handler: Handler
    private var playerState = STATE_DEFAULT

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

        val track: Track = getSerializable(this, "track", Track::class.java)
        preparePlayer(track.previewUrl)

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
            object: Runnable {
                override fun run() {
                    if (playerState == STATE_PLAYING) {
                        setCurrentPosition(mediaPlayer.currentPosition)
                    }
                    handler.postDelayed(this, DALAY_TIMER)
                }
            },
            DALAY_TIMER
        )
        playBtn.setOnClickListener {
            playbackControl()
        }
        mediaPlayer.setOnCompletionListener {
            stoppedPlayer()
            setCurrentPosition(0)
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBtn.setImageResource(R.drawable.play_btn)
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn.setImageResource(R.drawable.play_btn)
            playerState = STATE_PREPARED
        }
    }

    private fun setCurrentPosition(currPosition: Int) {
        timeLeft.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currPosition)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
            STATE_DEFAULT -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playBtn.setImageResource(R.drawable.pause_btn)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playBtn.setImageResource(R.drawable.play_btn)
        playerState = STATE_PAUSED
    }

    private fun stoppedPlayer() {
        playBtn.setImageResource(R.drawable.play_btn)
        playerState = STATE_PREPARED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}