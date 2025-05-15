package com.example.playlistmaker.library.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.utils.pxToDp

class PlaylistViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.text_name_playlist)
    private val countTitle: TextView = itemView.findViewById(R.id.text_count_playlist)
    private val image: ImageView = itemView.findViewById(R.id.image_playlist)

    constructor(
        parent: ViewGroup
    ) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.playlist_card, parent, false)
    )

    fun bind(playlist: Playlist) {
        title.text = playlist.name
        val textCount = itemView.context.getString(R.string.playlist_count_tracks)
        countTitle.text = String.format(textCount, playlist.countTracks)
        val multiTransform = MultiTransformation(CenterCrop(), RoundedCorners(pxToDp(8f, itemView.context)))
        Glide.with(itemView.context)
            .load(playlist.imagePath)
            .placeholder(R.drawable.placeholder)
            .transform(multiTransform)
            .into(image)
    }
}