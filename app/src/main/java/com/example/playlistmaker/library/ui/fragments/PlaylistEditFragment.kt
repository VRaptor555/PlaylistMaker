package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState
import com.example.playlistmaker.library.ui.view_model.PlaylistEditViewModel
import com.example.playlistmaker.utils.getSerializable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditFragment: PlaylistCreateFragment() {

    override val playlistViewModel: PlaylistEditViewModel by viewModel() {
        parametersOf(getSerializable(requireArguments(), ARGS_PLAYLIST, Playlist::class.java))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistCreateState.ChangePlaylist -> changePlaylist(it.playlist)
            }
        }
        binding.home.text = getString(R.string.edit_playlist_button)
        binding.createButton.text = getString(R.string.update_playlist_button)
        binding.createButton.setOnClickListener {
            lifecycleScope.launch {
                if (playlistViewModel.savePlaylistToDb()) {
                    Toast.makeText(requireContext(),
                        String.format(getString(R.string.playlist_edited), playlistName),
                        Toast.LENGTH_SHORT).show()
                    toExit()
                }
            }
        }

    }

    override fun changePlaylist(playlist: Playlist) {
        super.changePlaylist(playlist)
        backPressCallback.isEnabled = false
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist_edit"

        fun createArgs(playlist: Playlist): Bundle = bundleOf(ARGS_PLAYLIST to playlist)
    }

}