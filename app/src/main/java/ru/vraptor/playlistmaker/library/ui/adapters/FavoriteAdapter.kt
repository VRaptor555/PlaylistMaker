package ru.vraptor.playlistmaker.library.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.vraptor.playlistmaker.search.domain.model.Track

class FavoriteAdapter(private val clickListener: FavoriteClickListener) : RecyclerView.Adapter<FavoriteViewHolder>() {
    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface FavoriteClickListener {
        fun onTrackClick(track: Track)
    }
}