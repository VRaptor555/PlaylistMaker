package com.example.playlistmaker.library.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState
import com.example.playlistmaker.library.ui.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.main.ui.CustomTextWatcher
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.utils.pxToDp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaylistCreateFragment: BindingFragments<FragmentPlaylistCreateBinding>() {
    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel()
//    private lateinit var nameTextWatcher: CustomTextWatcher
//    private lateinit var descriptionTextWatcher: CustomTextWatcher

    private var playlistName: String = ""
    private var playlistImageUrl: String? = null
    private var confirmDialog: MaterialAlertDialogBuilder? = null

    private var backPressCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showConfirmDialog()
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistCreateBinding {
        return FragmentPlaylistCreateBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistCreateViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistCreateState.ChangePlaylist -> changePlaylist(it.playlist)
            }
        }

        binding.nameText.addTextChangedListener(object: CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                playlistCreateViewModel.changeName(s?.toString() ?: "")
            }
        })
        binding.descriptionText.addTextChangedListener(object: CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                playlistCreateViewModel.changeDescription(s?.toString())
            }
        })

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val privateUri = saveImageToPrivateStorage(uri)
                 playlistCreateViewModel.changeImage(privateUri)
            }
        }
        binding.placeholderImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            lifecycleScope.launch {
                if (playlistCreateViewModel.savePlaylistToDb()) {
                    Toast.makeText(requireContext(),
                        String.format(getString(R.string.playlist_created), playlistName),
                        Toast.LENGTH_SHORT).show()
                    toExit()
                } else {
                    Toast.makeText(requireContext(), R.string.playlist_name_already, Toast.LENGTH_SHORT).show()
                }
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(resources.getString(R.string.playlist_confirm))
            .setMessage(R.string.playlist_confirm_message)
            .setNegativeButton(resources.getString(R.string.confirm_yes)) { _, _ ->
                toExit()
            }
            .setPositiveButton(resources.getString(R.string.confirm_no)) { _, _ ->

            }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
        backPressCallback.isEnabled = false
        binding.home.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showConfirmDialog() {
        confirmDialog?.show()
    }

    private fun toExit() {
        backPressCallback.isEnabled = false
        if (activity is PlayerActivity) {
            parentFragmentManager.popBackStack()
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (activity is PlayerActivity) {
            (activity as PlayerActivity).handlerAction(playlistCreateViewModel.playlist)
        }
    }

    private fun changePlaylist(playlist: Playlist) {
        binding.nameHint.isVisible = playlist.name.isNotEmpty()
        binding.descriptionHint.isVisible = !(playlist.description == null || playlist.description?.isEmpty() == true)
        binding.nameText.isSelected = binding.nameHint.isVisible
        binding.descriptionText.isSelected = binding.descriptionHint.isVisible
        if (playlist.name != playlistName) {
            playlistName = playlist.name
            binding.createButton.isEnabled = playlistName.isNotEmpty()
        }
        if (playlist.imagePath != playlistImageUrl) {
            playlistImageUrl = playlist.imagePath
            val imageUri = if (playlistImageUrl != null) {
                playlistImageUrl?.toUri()
            } else {
                null
            }
            val multiTransform = MultiTransformation(CenterCrop(), RoundedCorners(pxToDp(8f, requireContext())))
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.playlist_add_placeholder)
                .transform(multiTransform)
                .into(binding.placeholderImage)

        }
        backPressCallback.isEnabled =
            playlist.name.isNotEmpty()
                    || playlist.imagePath != null
                    || playlist.description != null
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveImageToPrivateStorage(uri: Uri): Uri {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${Uuid.random()}.jpg")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }
}