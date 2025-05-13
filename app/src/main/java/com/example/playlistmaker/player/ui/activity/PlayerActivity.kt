package com.example.playlistmaker.player.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.fragments.PlaylistCreateFragment
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.player.ui.adapters.PlaylistBottomAdapter
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.dpToPx
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.getURLImage500
import com.example.playlistmaker.utils.timeMillisToMin
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!
    private var _bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheet get() = _bottomSheet!!

    private var _adapter: PlaylistBottomAdapter? = null
    private val adapter get() = _adapter!!

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = getSerializable(this, ARGS_TRACK, Track::class.java)
        isFavorite = track.isFavorite
        val viewModel: PlayerViewModel by viewModel {
            parametersOf(track)
        }
        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observePlaylistState().observe(this) {
            showPlaylist(it)
        }
        _bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)

        bottomSheet.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        blackout(false)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        blackout(false)
                        viewModel.playlistLoad()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        blackout(true)
                    }
                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) {
                    binding.main.alpha = 0.5f
                } else {
                    binding.main.alpha = slideOffset * -0.5f + 0.5f
                }
            }
        })

        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        binding.btnBack.setOnClickListener {
            this.finish()
        }

        _adapter = PlaylistBottomAdapter(object: PlaylistBottomAdapter.PlaylistBottomClickListener {
            override fun onPlaylistBottomClick(playlist: Playlist) {
                viewModel.onAddToPlaylist(playlist)
                bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            }
        })

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
        binding.favoriteBtn.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        binding.queueBtn.setOnClickListener {
            addTrackToPlaylist()
        }
        binding.newPlaylist.setOnClickListener {
            addPlaylist()
        }
        binding.playlistItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.playlistItems.adapter = adapter
        viewModel.initPlayer()
    }

    private fun addPlaylist() {
        binding.main.visibility = View.GONE
        binding.bottomSheet.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.player_fragment_container, PlaylistCreateFragment())
            .addToBackStack("player")
            .commit()
    }

    private fun blackout(fillBlackout: Boolean) {
        binding.main.isClickable = fillBlackout
        binding.main.isFocusable = fillBlackout
        binding.main.isEnabled = fillBlackout
    }

    private fun changeStatePlaying(playBtnVisible: Boolean, hint: String, progress: String) {
        binding.playBtn.isVisible = playBtnVisible
        binding.progressBar.isVisible = !playBtnVisible
        binding.timeLeft.isVisible = playBtnVisible
        binding.timeLeft.text = progress
        if (hint == "PLAY") {
            binding.playBtn.setImageResource(R.drawable.play_btn)
        } else {
            binding.playBtn.setImageResource(R.drawable.pause_btn)
        }
    }

    private fun changeFavorite(favorite: Boolean) {
        isFavorite = favorite
        if (favorite) {
            binding.favoriteBtn.setImageResource(R.drawable.favorite_active_btn)
        } else {
            binding.favoriteBtn.setImageResource(R.drawable.favorite_btn)
        }
    }

    private fun render(state: PlayerState) {
        changeStatePlaying(state.isPlayButtonEnable, state.buttonText, state.progress)
        if (isFavorite != state.isFavorite) {
            changeFavorite(state.isFavorite)
        }
    }

    private fun addTrackToPlaylist() {
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylist(playlistState: PlaylistState) {
        when(playlistState) {
            is PlaylistState.Empty -> {
                adapter.playlist.clear()
                adapter.notifyDataSetChanged()
            }
            is PlaylistState.Loading -> {}
            is PlaylistState.Error -> {}
            is PlaylistState.Content -> {
                adapter.playlist.clear()
                adapter.playlist.addAll(playlistState.playlist)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private const val ARGS_TRACK = "track"

        fun createArgs(track: Track): Bundle = bundleOf(
            ARGS_TRACK to track,
        )
    }
}