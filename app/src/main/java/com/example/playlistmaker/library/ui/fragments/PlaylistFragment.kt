package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: BindingFragments<FragmentPlaylistBinding>() {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private lateinit var onAddClickDebounce: (Int) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddClickDebounce = debounce<Int>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            clickAdd()
        }

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> TODO()
                is PlaylistState.Empty -> showEmpty()
                is PlaylistState.Error -> TODO()
                is PlaylistState.Loading -> TODO()
            }
        }
        binding.btnAddPlaylist.setOnClickListener {
            onAddClickDebounce(1)
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholder.visibility = View.VISIBLE
            playlist.visibility = View.GONE
        }
    }

    private fun clickAdd() {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistAddFragment
        )
    }

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

}