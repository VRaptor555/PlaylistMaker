package ru.vraptor.playlistmaker.player.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vraptor.playlistmaker.library.domain.model.Playlist


class PlaylistBottomAdapter(
    private val clickListener: PlaylistBottomClickListener
): RecyclerView.Adapter<PlaylistBottomViewHolder>() {
    var playlist: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomViewHolder {
        return PlaylistBottomViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistBottomViewHolder,
        position: Int
    ) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistBottomClick(playlist[position]) }
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    interface PlaylistBottomClickListener {
        fun onPlaylistBottomClick(playlist: Playlist)
    }
}