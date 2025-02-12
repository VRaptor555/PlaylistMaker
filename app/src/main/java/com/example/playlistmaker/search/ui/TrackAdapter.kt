package com.example.playlistmaker.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.model.Track

class TrackAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: MutableList<Track> =  mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}