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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistCreateState
import com.example.playlistmaker.library.ui.view_model.PlaylistCreateViewModel
import com.example.playlistmaker.main.ui.fragments.BindingFragments
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaylistCreateFragment: BindingFragments<FragmentPlaylistCreateBinding>() {
    private val playlistCreateViewModel: PlaylistCreateViewModel by viewModel()
    private var nameTextWatcher: TextWatcher? = null
    private var descriptionTextWatcher: TextWatcher? = null

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
                is PlaylistCreateState.Cancel -> cancelCreatePlaylist()
                is PlaylistCreateState.ChangeImage -> changeImage(it.imagePath)
                is PlaylistCreateState.Filled -> filledName(it.filled)
                is PlaylistCreateState.SavePlaylist -> savePlayList(it.playlist)
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
            playlistCreateViewModel.savePlaylistToDB()

        }
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

    private fun cancelCreatePlaylist() {

    }

    private fun changeImage(imagePath: Uri) {
        binding.placeholderImage.setImageURI(imagePath)
    }

    private fun filledName(filled: Boolean) {
        binding.createButton.isEnabled = filled
    }

    private fun savePlayList(playlist: Playlist) {

    }
}