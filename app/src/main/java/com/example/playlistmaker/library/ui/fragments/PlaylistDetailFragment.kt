package com.example.playlistmaker.library.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
import com.example.playlistmaker.player.ui.adapters.PlaylistBottomAdapter
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.utils.debounce
import com.example.playlistmaker.utils.getSerializable
import com.example.playlistmaker.utils.pxToDp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : BindingFragments<FragmentPlaylistDetailBinding>() {
    private val playlistDetailViewModel: PlaylistDetailViewModel by viewModel() {
        parametersOf(getSerializable(requireArguments(), ARGS_PLAYLIST, Playlist::class.java))
    }

    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var onShareClickDebounce: (Int) -> Unit

    private var tracksAdapter: TrackAdapter? = null
    private var playlistAdapter: PlaylistBottomAdapter? = null
    private var _bottomTracklist: BottomSheetBehavior<LinearLayout>? = null
    private val bottomTracklist get() = _bottomTracklist!!
    private var _bottomMenu: BottomSheetBehavior<LinearLayout>? = null
    private val bottomMenu get() = _bottomMenu!!
    private var confirmDeleteTrackDialog: MaterialAlertDialogBuilder? = null
    private var confirmDeletePlaylistDialog: MaterialAlertDialogBuilder? = null

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

        onShareClickDebounce = debounce<Int>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { _ ->
            bottomMenu.state = BottomSheetBehavior.STATE_HIDDEN
            playlistDetailViewModel.sharePlaylist()
        }

        playlistDetailViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

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

        playlistAdapter = PlaylistBottomAdapter(
            object : PlaylistBottomAdapter.PlaylistBottomClickListener {
                override fun onPlaylistBottomClick(playlist: Playlist) {

                }
            }
        )

        binding.tracksItems.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksItems.adapter = tracksAdapter
        binding.playlistItem.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.playlistItem.adapter = playlistAdapter

        _bottomTracklist = BottomSheetBehavior.from(binding.bottomTracklist)
        bottomTracklist.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
        bottomTracklist.state = BottomSheetBehavior.STATE_COLLAPSED

        _bottomMenu = BottomSheetBehavior.from(binding.bottomMenu)
        bottomMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        blackout(false)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        blackout(false)
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
                    binding.overlay.alpha = 0.6f
                } else {
                    binding.overlay.alpha = slideOffset * 0.6f + 0.6f
                }
            }
        })
        bottomMenu.state = BottomSheetBehavior.STATE_HIDDEN

        confirmDeleteTrackDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(resources.getString(R.string.playlist_detail_delete_track))
            .setNegativeButton(resources.getString(R.string.res_no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.res_yes)) { _, _ ->
            }

        confirmDeletePlaylistDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setNegativeButton(resources.getString(R.string.res_no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.res_yes)) { _, _ ->
                deleteThisPlaylist()
            }

        binding.buttonShare.setOnClickListener {
            onShareClickDebounce(1)
        }

        binding.menuShare.setOnClickListener {
            bottomMenu.state = BottomSheetBehavior.STATE_HIDDEN
            playlistDetailViewModel.sharePlaylist()
        }

        binding.menuDelete.setOnClickListener {
            bottomMenu.state = BottomSheetBehavior.STATE_HIDDEN
            confirmDeletePlaylist(playlistDetailViewModel.playlist)
        }

        binding.menuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistDetailFragment_to_playlistEditFragment,
                PlaylistEditFragment.createArgs(playlistDetailViewModel.playlist)
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSetting.setOnClickListener {
            bottomMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            playlistDetailViewModel.updatePlaylist()
            playlistDetailViewModel.loadingPlaylist()
        }
    }

    private fun deleteThisPlaylist() {
        playlistDetailViewModel.deletePlaylist()
    }

    private fun blackout(fillBlackout: Boolean) {
        binding.constraintLayout.isClickable = fillBlackout
        binding.constraintLayout.isFocusable = fillBlackout
        binding.constraintLayout.isEnabled = fillBlackout
        binding.bottomTracklist.isVisible = fillBlackout
    }


    private fun confirmDeletePlaylist(playlist: Playlist) {
        confirmDeletePlaylistDialog?.setTitle(
        String.format(
            resources.getString(
                R.string.playlist_delete_confirm), playlist.name)
        )
        confirmDeletePlaylistDialog?.show()
    }

    private fun confirmDeleteTrack(track: Track) {
        confirmDeleteTrackDialog?.setTitle(
            String.format(
                resources.getString(
                    R.string.playlist_detail_delete_track), track.trackName)
        )?.setPositiveButton(resources.getString(R.string.res_yes)) { _, _ ->
            playlistDetailViewModel.deleteTrack(track)
        }
        confirmDeleteTrackDialog?.show()
    }

    private fun render(state: PlaylistDetailState) {
        when (state) {
            is PlaylistDetailState.Loading -> showLoading(state.playlist)
            is PlaylistDetailState.Content -> showContent(state.tracks)
            is PlaylistDetailState.SendMessage -> sendMessageToast(state.message)
            is PlaylistDetailState.ShareIntent -> sharing(state.share)
            is PlaylistDetailState.ToExit -> findNavController().popBackStack()
        }
    }

    private fun sharing(share: Intent) {
        startActivity(Intent.createChooser(share, null))
    }

    private fun sendMessageToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
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
        playlistAdapter?.playlist?.clear()
        playlistAdapter?.playlist?.add(playlist)
        playlistAdapter?.notifyDataSetChanged()
        showContent(emptyList())
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
        binding.placeholderNoTrack.isVisible = tracks.isEmpty()
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