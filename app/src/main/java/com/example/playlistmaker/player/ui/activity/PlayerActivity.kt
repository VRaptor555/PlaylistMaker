package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {

    private var binding: ActivityPlayerBinding? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnBack?.setOnClickListener {
            this.finish()
        }
        val track = getSerializable(this, ARGS_TRACK, Track::class.java)
        isFavorite = track.isFavorite
        val viewModel: PlayerViewModel by viewModel() {
            parametersOf(track)
        }
        viewModel.observeState().observe(this) {
            render(it)
        }

        binding?.let {
            it.timeLeft.text = timeMillisToMin(track.trackTimeMillis)
            it.durationInfo.text = timeMillisToMin(track.trackTimeMillis)
            it.albumInfo.text = track.collectionName
            it.yearInfo.text =
                LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            it.genreInfo.text = track.primaryGenreName
            it.countryInfo.text = track.country
            it.trackTitle.text = track.trackName
            it.trackArtist.text = track.artistName

            Glide.with(baseContext)
                .load(getURLImage500(track.artworkUrl100))
                .placeholder(R.drawable.player_placeholder)
                .transform(RoundedCorners(dpToPx(8f, baseContext)))
                .into(it.imageTrack)

            it.playBtn.setOnClickListener {
                viewModel.play()
            }
            it.favoriteBtn.setOnClickListener {
                viewModel.onFavoriteClicked()
            }
        }

        viewModel.initPlayer()
    }

    private fun changeStatePlaying(playBtnVisible: Boolean, hint: String, progress: String) {
        binding?.playBtn?.isVisible = playBtnVisible
        binding?.progressBar?.isVisible = !playBtnVisible
        binding?.timeLeft?.isVisible = playBtnVisible
        binding?.timeLeft?.text = progress
        if (hint == "PLAY") {
            binding?.playBtn?.setImageResource(R.drawable.play_btn)
        } else {
            binding?.playBtn?.setImageResource(R.drawable.pause_btn)
        }
    }

    private fun changeFavorite(favorite: Boolean) {
        isFavorite = favorite
        if (favorite) {
            binding?.favoriteBtn?.setImageResource(R.drawable.favorite_active_btn)
        } else {
            binding?.favoriteBtn?.setImageResource(R.drawable.favorite_btn)
        }
    }

    private fun render(state: PlayerState) {
        changeStatePlaying(state.isPlayButtonEnable, state.buttonText, state.progress)
        if (isFavorite != state.isFavorite) {
            changeFavorite(state.isFavorite)
        }
    }

    companion object {
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle = bundleOf(
            ARGS_TRACK to track,
        )
    }
}