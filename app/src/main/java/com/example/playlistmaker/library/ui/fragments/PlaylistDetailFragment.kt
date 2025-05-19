package com.example.playlistmaker.library.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistDetailState
import com.example.playlistmaker.library.ui.view_model.PlaylistDetailViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.pxToDp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : BindingFragments<FragmentPlaylistDetailBinding>() {
    private val playlistDetailViewModel: PlaylistDetailViewModel by viewModel() {
        parametersOf(getSerializable(requireArguments(), ARGS_PLAYLIST, Playlist::class.java))
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private var tracksAdapter: TrackAdapter? = null
    private var _bottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheet get() = _bottomSheet!!
    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistDetailBinding {
        return FragmentPlaylistDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> clickTrack(track) }

        tracksAdapter = TrackAdapter(
            object : TrackAdapter.TrackClickListener {
                override fun onTrackClick(track: Track) {
                    onTrackClickDebounce(track)
                }

                override fun onLongTrackClick(track: Track): Boolean {
                    confirmDeleteTrack(track)
                    return true
                }
            }
        )

        binding.tracksItems.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksItems.adapter = tracksAdapter

        _bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

        playlistDetailViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(resources.getString(R.string.playlist_detail_delete_track))
            .setNegativeButton(resources.getString(R.string.res_no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.res_yes)) { _, _ ->
            }

        playlistDetailViewModel.showContext()
    }

    private fun confirmDeleteTrack(track: Track) {
        confirmDialog?.setPositiveButton(resources.getString(R.string.res_yes)) { _, _ ->
            toDelete(track)
        }
        confirmDialog?.show()
    }

    private fun toDelete(track: Track) {
        playlistDetailViewModel.deleteTrack(track)
    }

    private fun render(state: PlaylistDetailState) {
        when (state) {
            is PlaylistDetailState.Loading -> showLoading(state.playlist)
            is PlaylistDetailState.Context -> showContent(state.tracks)
        }
    }

    private fun showLoading(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistCount.text =
            String.format(getString(R.string.playlist_count_tracks), playlist.countTracks)

        val multiTransform = MultiTransformation(CenterCrop(), RoundedCorners(pxToDp(8f, requireContext())))
        Glide.with(this)
            .load(playlist.imagePath)
            .placeholder(R.drawable.placeholder)
            .transform(multiTransform)
            .into(binding.imagePlaylist)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        var timeTracks = 0L
        for (track in tracks) {
            timeTracks += track.trackTimeMillis.toLong()
        }
        binding.playlistTime.text =
            String.format(getString(R.string.playlist_time_tracks), timeTracks / 60000)
        tracksAdapter?.tracks?.clear()
        tracksAdapter?.tracks?.addAll(tracks)
        tracksAdapter?.notifyDataSetChanged()
    }

    private fun clickTrack(track: Track) {
        findNavController().navigate(
            R.id.action_playlistDetailFragment_to_playerActivity,
            PlayerActivity.createArgs(track)
        )

    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L

        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }
}