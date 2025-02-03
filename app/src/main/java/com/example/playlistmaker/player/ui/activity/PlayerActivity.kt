package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.getURLImage500
import com.example.playlistmaker.utils.timeMillisToMin
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            this.finish()
        }

        val track = getSerializable(this, "track", Track::class.java)

        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(track.previewUrl)) [PlayerViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.timeLeft.text = timeMillisToMin(track.trackTimeMillis)
        binding.durationInfo.text = timeMillisToMin(track.trackTimeMillis)
        binding.albumInfo.text = track.collectionName
        binding.yearInfo.text =
            LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
        binding.genreInfo.text = track.primaryGenreName
        binding.countryInfo.text = track.country
        binding.trackTitle.text = track.trackName
        binding.trackArtist.text = track.artistName

        Glide.with(baseContext)
            .load(getURLImage500(track.artworkUrl100))
            .placeholder(R.drawable.player_placeholder)
            .transform(RoundedCorners(dpToPx(8f, baseContext)))
            .into(binding.imageTrack)

        binding.playBtn.setOnClickListener {
            viewModel.play()
        }

        viewModel.initPlayer()
    }

    private fun loading() {
        binding.playBtn.isVisible = false
        binding.timeLeft.text = "-:--"
    }

    private fun setPaused(timecode: String) {
        binding.playBtn.isVisible = true
        binding.playBtn.setImageResource(R.drawable.play_btn)
        binding.timeLeft.text = timecode
    }

    private fun setPlaying(timecode: String) {
        binding.playBtn.isVisible = true
        binding.playBtn.setImageResource(R.drawable.pause_btn)
        binding.timeLeft.text = timecode
    }

    private fun render(state: PlayerState) {
        when(state) {
            is PlayerState.IsPause -> setPaused(state.timeCode)
            is PlayerState.IsPlay -> setPlaying(state.timeCode)
            is PlayerState.Loading -> loading()
        }
    }
}