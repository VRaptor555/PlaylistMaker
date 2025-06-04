package ru.vraptor.playlistmaker.library.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vraptor.playlistmaker.library.domain.model.Playlist

class PlaylistAdapter(
    private val clickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlist: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlist[position]) }
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}