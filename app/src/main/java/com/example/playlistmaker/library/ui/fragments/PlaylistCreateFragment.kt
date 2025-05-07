package com.example.playlistmaker.library.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState
import com.example.playlistmaker.library.ui.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaylistCreateFragment: BindingFragments<FragmentPlaylistCreateBinding>() {
    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel()
    private var nameTextWatcher: TextWatcher? = null
    private var descriptionTextWatcher: TextWatcher? = null

    private var playlistName: String = ""
    private var playlistImageUrl: String? = null
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val backPressCallback: OnBackPressedCallback = object: OnBackPressedCallback(true) {
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

        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playlistCreateViewModel.changeName(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playlistCreateViewModel.changeDescription(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        nameTextWatcher?.let { binding.nameText.addTextChangedListener(it)}
        descriptionTextWatcher?.let { binding.descriptionText.addTextChangedListener(it) }

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
                    backPressCallback.isEnabled = false
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Такое имя уже есть!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.playlist_confirm))
            .setMessage(R.string.playlist_confirm_message)
            .setNegativeButton(resources.getString(R.string.confirm_yes)) { _, _ ->
                backPressCallback.isEnabled = false
                findNavController().popBackStack()
            }
            .setPositiveButton(resources.getString(R.string.confirm_no)) { _, _ ->

            }
        activity?.onBackPressedDispatcher?.addCallback(backPressCallback)
        backPressCallback.isEnabled = false
    }

    private fun showConfirmDialog() {
        if (playlistName.isNotEmpty()) {
            confirmDialog.show()
        }
    }

    private fun changePlaylist(playlist: Playlist) {
        if (playlist.name != playlistName) {
            playlistName = playlist.name
            binding.createButton.isEnabled = playlistName.isNotEmpty()
        }
        if (playlist.imagePath != playlistImageUrl) {
            playlistImageUrl = playlist.imagePath
            if (playlistImageUrl != null) {
                binding.placeholderImage.setImageURI(playlistImageUrl?.toUri())
            } else {
                binding.placeholderImage.setImageResource(R.drawable.playlist_add_placeholder)
            }
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