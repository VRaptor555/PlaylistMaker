package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: BindingFragments<FragmentPlaylistBinding>() {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> TODO()
                is PlaylistState.Empty -> showEmpty()
                is PlaylistState.Error -> TODO()
                is PlaylistState.Loading -> TODO()
            }
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholder.visibility = View.VISIBLE
            playlist.visibility = View.GONE
        }
    }


    companion object {
        fun newInstance() = PlaylistFragment()
    }

}