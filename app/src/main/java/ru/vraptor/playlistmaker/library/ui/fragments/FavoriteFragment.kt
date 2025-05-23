package ru.vraptor.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import ru.vraptor.playlistmaker.R
import ru.vraptor.playlistmaker.databinding.FragmentFavoriteBinding
import ru.vraptor.playlistmaker.library.ui.adapters.FavoriteAdapter
import ru.vraptor.playlistmaker.library.ui.models.FavoriteState
import ru.vraptor.playlistmaker.library.ui.view_model.FavoriteViewModel
import ru.vraptor.playlistmaker.main.ui.fragments.BindingFragments
import ru.vraptor.playlistmaker.main.ui.utils.PlaylistTracksCallback
import ru.vraptor.playlistmaker.player.ui.activity.PlayerActivity
import ru.vraptor.playlistmaker.search.domain.model.Track
import ru.vraptor.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: BindingFragments<FragmentFavoriteBinding>() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var adapter: FavoriteAdapter? = null
    private lateinit var onFavoriteClickDebounce: (Track) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFavoriteClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> clickTrack(track) }

        adapter = FavoriteAdapter(
            object : FavoriteAdapter.FavoriteClickListener {
                override fun onTrackClick(track: Track) {
                    onFavoriteClickDebounce(track)
                }
            }
        )

        binding.tracksList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = adapter

        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholder.visibility = View.VISIBLE
            tracksList.visibility = View.GONE
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.apply {
            placeholder.visibility = View.GONE
            tracksList.visibility = View.VISIBLE
        }
        adapter?.let {
            val diffTrackCalback = PlaylistTracksCallback(it.tracks.toList(), tracks)
            val diffTracks = DiffUtil.calculateDiff(diffTrackCalback)
            it.tracks.clear()
            it.tracks.addAll(tracks)
            diffTracks.dispatchUpdatesTo(it)
        }
    }

    private fun clickTrack(track: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerActivity,
            PlayerActivity.createArgs(track)
        )
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.showFavorite()
    }

    private fun render(state: FavoriteState) {
        when(state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty()
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}