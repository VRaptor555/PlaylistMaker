package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.example.playlistmaker.main.ui.utils.PlaylistCallback
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: BindingFragments<FragmentPlaylistBinding>() {
    private val playlistViewModel: PlaylistViewModel by viewModel()
    private lateinit var onAddClickDebounce: (Int) -> Unit
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private var adapter: PlaylistAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            clickAdd()
        }
        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            clickPlaylist(playlist)
        }
        adapter = PlaylistAdapter(
            object : PlaylistAdapter.PlaylistClickListener{
                override fun onPlaylistClick(playlist: Playlist) {
                    onPlaylistClickDebounce(playlist)
                }
            }
        )

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistState.Content -> showContent(it.playlist)
                is PlaylistState.Empty -> showEmpty()
                is PlaylistState.Error -> showError(it.errorMessage)
                is PlaylistState.Loading -> showLoading()
            }
        }
        binding.btnAddPlaylist.setOnClickListener {
            onAddClickDebounce(1)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter
    }

    private fun showEmpty() {
        binding.apply {
            placeholderFound.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.apply {
            placeholderFound.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), String.format(getString(R.string.playlist_error), message), Toast.LENGTH_LONG).show()
        showEmpty()
    }

    private fun clickAdd() {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistAddFragment,
        )
    }

    private fun clickPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistDetailFragment,
            PlaylistDetailFragment.createArgs(playlist)
        )
    }

    private fun showContent(content: List<Playlist>) {
        binding.apply {
            placeholderFound.visibility = View.GONE
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        adapter?.let {
            val diffPlaylistCallback = PlaylistCallback(it.playlist.toList(), content)
            val diffPlaylist = DiffUtil.calculateDiff(diffPlaylistCallback)
            it.playlist.clear()
            it.playlist.addAll(content)
            diffPlaylist.dispatchUpdatesTo(it)
        }
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.showPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

}