package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.library.ui.models.FavoriteState
import com.example.playlistmaker.library.ui.view_model.FavoriteViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: BindingFragments<FragmentFavoriteBinding>() {
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoriteState.Content -> TODO()
                is FavoriteState.Empty -> showEmpty()
                is FavoriteState.Error -> TODO()
                is FavoriteState.Loading -> TODO()
            }
        }
    }

    private fun showEmpty() {
        binding.apply {
            placeholder.visibility = View.VISIBLE
            libraryList.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}